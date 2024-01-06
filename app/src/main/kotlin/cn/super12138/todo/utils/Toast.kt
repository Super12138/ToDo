import android.content.Context
import android.widget.Toast
import cn.super12138.todo.ToDoApplication

fun String.showToast(
    context: Context = ToDoApplication.context,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, this, duration).show()
}

fun Int.showToast(context: Context = ToDoApplication.context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}