package cn.super12138.todo.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.constant.Constants.DEFAULT_VIEW_TYPE
import cn.super12138.todo.constant.Constants.EMPTY_VIEW_TYPE
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.showToast
import cn.super12138.todo.views.fragments.ToDoBottomSheet
import cn.super12138.todo.views.viewmodels.ProgressViewModel
import cn.super12138.todo.views.viewmodels.ToDoViewModel

class ToDoAdapter(
    private val todoList: MutableList<ToDo>,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 默认待办项
    inner class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContext: TextView = view.findViewById(R.id.todo_content)
        val todoSubject: TextView = view.findViewById(R.id.todo_subject)
        val checkToDoBtn: Button = view.findViewById(R.id.check_item_btn)
    }

    // 空项目提示
    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // 判断列表是否为空，为空显示空项目提示
    override fun getItemViewType(position: Int): Int {
        return if (todoList.isEmpty()) EMPTY_VIEW_TYPE else DEFAULT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_VIEW_TYPE) { // 如果列表是空的
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_empty, parent, false)
            EmptyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
            DefaultViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 判断当前的holder是不是待办项目的holder
        if (holder is DefaultViewHolder) {
            val todo = todoList[position]
            holder.todoContext.text = todo.content
            holder.todoSubject.text = todo.subject

            val progressViewModel =
                ViewModelProvider(viewModelStoreOwner)[ProgressViewModel::class.java]
            val todoViewModel =
                ViewModelProvider(viewModelStoreOwner)[ToDoViewModel::class.java]

            holder.checkToDoBtn.setOnClickListener {
                VibrationUtils.performHapticFeedback(it)

                if (position >= todoList.size) {
                    return@setOnClickListener
                }

                todoList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, todoList.size)

                todoViewModel.updateTaskState(todo.uuid)

                progressViewModel.updateProgress()
            }

            holder.itemView.setOnClickListener {
                if (GlobalValues.devMode) {
                    VibrationUtils.performHapticFeedback(it)
                    "Current position: $position".showToast()
                }
            }

            holder.itemView.setOnLongClickListener {
                val toDoBottomSheet = ToDoBottomSheet.newInstance(
                    true,
                    position,
                    todo.uuid,
                    todo.state,
                    todo.subject,
                    todo.content
                )
                toDoBottomSheet.show(fragmentManager, ToDoBottomSheet.TAG)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return if (todoList.isEmpty()) 1 else todoList.size
    }
}