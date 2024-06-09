package cn.super12138.todo.views.todo

import android.view.HapticFeedbackConstants
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
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.showToast
import cn.super12138.todo.views.bottomsheet.ToDoBottomSheet
import cn.super12138.todo.views.progress.ProgressFragmentViewModel

class ToDoAdapter(
    private val todoList: MutableList<ToDo>,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContext: TextView = view.findViewById(R.id.todo_content)
        val todoSubject: TextView = view.findViewById(R.id.todo_subject)
        val checkToDoBtn: Button = view.findViewById(R.id.check_item_btn)
        // val delToDoBtn: Button = view.findViewById(R.id.delete_item_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoContext.text = todo.content
        holder.todoSubject.text = todo.subject
        /*if (!todo.isAnimated) {
            holder.itemView.alpha = 0f
            holder.itemView.animate().alpha(1f).duration = 200
            todo.isAnimated = true
        }*/

        val progressViewModel =
            ViewModelProvider(viewModelStoreOwner)[ProgressFragmentViewModel::class.java]
        val todoViewModel =
            ViewModelProvider(viewModelStoreOwner)[ToDoFragmentViewModel::class.java]

        holder.checkToDoBtn.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            if (position < 0 || position >= todoList.size) {
                return@setOnClickListener
            }

            todoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, todoList.size)

            todoViewModel.updateTaskState(todo.uuid)

            // 设置空项目提示可见性
            if (todoList.isEmpty()) {
                todoViewModel.emptyTipVis.value = View.VISIBLE
            } else {
                todoViewModel.emptyTipVis.value = View.GONE
            }
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

    override fun getItemCount() = todoList.size
}