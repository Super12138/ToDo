package cn.super12138.todo.views.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.constant.Constants.DEFAULT_VIEW_TYPE
import cn.super12138.todo.constant.Constants.EMPTY_VIEW_TYPE
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.todo.ToDoAdapter.DefaultViewHolder

class AllTasksAdapter(
    private val todoList: MutableList<ToDo>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 默认待办项
    inner class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContext: TextView = view.findViewById(R.id.todo_content)
        val todoSubject: TextView = view.findViewById(R.id.todo_subject)
        val itemBackground: LinearLayout = view.findViewById(R.id.item_background)
        val checkBtn: Button = view.findViewById(R.id.check_item_btn)
    }

    // 空项目提示
    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

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

    override fun getItemViewType(position: Int): Int {
        return if (todoList.isEmpty()) EMPTY_VIEW_TYPE else DEFAULT_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 判断当前的holder是不是待办项目的holder
        if (holder is DefaultViewHolder) {
            val todo = todoList[position]
            holder.apply {
                checkBtn.visibility = View.GONE
                todoContext.text = todo.content
                todoSubject.text = todo.subject
            }

            if (todo.state == 1) {
                holder.itemBackground.background =
                    ContextCompat.getDrawable(ToDoApp.context, R.drawable.bg_item_complete)
            }

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
        }
    }

    override fun getItemCount(): Int {
        return if (todoList.isEmpty()) 1 else todoList.size
    }
}