package cn.super12138.todo.views.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.views.progress.ProgressFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ToDoAdapter(val todoList: MutableList<ToDo>, val viewModelStoreOwner: ViewModelStoreOwner) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContext: TextView = view.findViewById(R.id.todo_context)
        val todoSubject: TextView = view.findViewById(R.id.todo_subject)
        val checkToDoBtn: Button = view.findViewById(R.id.check_item_btn)
        val delToDoBtn: Button = view.findViewById(R.id.delete_item_btn)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val progressViewModel =
            ViewModelProvider(viewModelStoreOwner).get(ProgressFragmentViewModel::class.java)
        val todoViewModel =
            ViewModelProvider(viewModelStoreOwner).get(ToDoFragmentViewModel::class.java)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        val holder = ViewHolder(view)

        holder.checkToDoBtn.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            if (position < 0 || position >= todoList.size) {
                return@setOnClickListener
            }
            val todo = todoList[position]

            todoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, todoList.size)

            GlobalScope.launch {
                Repository.updateStateByUUID(todo.uuid)
                progressViewModel.updateProgress()
            }

            // 设置空项目提示可见性
            if (todoList.isEmpty()) {
                todoViewModel.emptyTipVis.value = View.VISIBLE
            } else {
                todoViewModel.emptyTipVis.value = View.GONE
            }
        }

        holder.delToDoBtn.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            if (position < 0 || position >= todoList.size) {
                return@setOnClickListener
            }
            val todo = todoList[position]

            todoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, todoList.size)

            GlobalScope.launch {
                Repository.deleteByUUID(todo.uuid)
                progressViewModel.updateProgress()
            }


            // 设置空项目提示可见性
            if (todoList.isEmpty()) {
                todoViewModel.emptyTipVis.value = View.VISIBLE
            } else {
                todoViewModel.emptyTipVis.value = View.GONE
            }

            Snackbar.make(it, R.string.task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.delete_undo) {
                    if (todoList.size + 1 > 0) {
                        todoViewModel.emptyTipVis.value = View.GONE
                    }
                    todoList.add(ToDo(todo.uuid, todo.context, todo.subject))

                    todoViewModel.refreshData.value = 1

                    GlobalScope.launch {
                        Repository.insert(
                            ToDoRoom(
                                todo.uuid,
                                0,
                                todo.subject,
                                todo.context
                            )
                        )
                        progressViewModel.updateProgress()
                    }
                }
                .show()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoContext.text = todo.context
        holder.todoSubject.text = todo.subject
    }

    override fun getItemCount() = todoList.size
}