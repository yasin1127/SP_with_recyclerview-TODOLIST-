import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedpwithrv.databinding.ItemLayoutBinding

class TaskAdapter (private val tasklist:MutableList<Player>, private val clicklisten:TaskClickLister )
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()
{
    interface TaskClickLister {
        fun onEditClick(position:Int)
        fun onDeleteClick(position:Int)
    }

    class TaskViewHolder(val binding:ItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {

fun bind(task:Player){
    binding.Ntxt.text = task.Name
    binding.Atxt.text = task.Age.toString()
    binding.Stxt.text = task.Status
    binding.Rtxt.text= task.Rating.toString()

}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
   val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
   return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return tasklist.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var task =tasklist[position]
        holder.bind(task)
        holder.binding.add.setOnClickListener{
            clicklisten.onEditClick(position)
        }
        holder.binding.delete.setOnClickListener{
            clicklisten.onDeleteClick(position)
        }
        holder.binding.cb.isChecked= task.isCompleted
        holder.binding.cb.setOnCheckedChangeListener { _, isChecked ->
             task.isCompleted = isChecked
        }
    }

}