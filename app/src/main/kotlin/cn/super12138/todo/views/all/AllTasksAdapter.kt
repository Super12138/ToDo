package cn.super12138.todo.views.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.utils.VibrationUtils

class AllTasksAdapter(
    private val todoList: MutableList<ToDo>,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<AllTasksAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContext: TextView = view.findViewById(R.id.todo_content_all)
        val todoSubject: TextView = view.findViewById(R.id.todo_subject_all)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_tasks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoContext.text = todo.content
        holder.todoSubject.text = todo.subject

        holder.itemView.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            val infoBottomSheet = InfoBottomSheet.newInstance(
                todo.content,
                todo.subject,
                todo.state,
                todo.uuid
            )
            infoBottomSheet.show(fragmentManager, InfoBottomSheet.TAG)
        }

        /*if (!todo.isAnimated) {
            holder.itemView.alpha = 0f
            holder.itemView.animate().alpha(1f).duration = 200
            todo.isAnimated = true
        }*/
    }

    override fun getItemCount() = todoList.size
}