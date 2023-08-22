
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sneakers_shop_server.R
import com.sneakers_shop_server.model.SneakerOrderModel

class SneakerOrderAdapter(private val sneakers: List<SneakerOrderModel>) :
    RecyclerView.Adapter<SneakerOrderAdapter.ViewHolder>() {

    // La clase ViewHolder define las vistas que se mostrarán para cada elemento del RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.sneakerName)
        val gender: TextView = itemView.findViewById(R.id.sneakerGender)
        val price: TextView = itemView.findViewById(R.id.sneakerPrice)
        val quantity: TextView = itemView.findViewById(R.id.sneakerQuantity)
        val image: ImageView = itemView.findViewById(R.id.sneakerImage)
    }

    // onCreateViewHolder se llama cuando se crea una nueva vista para un elemento del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sneaker_item_request_each, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder se llama cuando se actualiza una vista con nuevos datos
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sneaker = sneakers[position]
        holder.name.text = sneaker.brand +" "+ sneaker.name
        holder.gender.text = "Gender - " + sneaker.gender
        holder.price.text = "Talla - " +sneaker.size.toString().replace(".0","")
        holder.quantity.text = sneaker.price.toString() +"€ x "+ sneaker.quantity.toString()+"u"
        holder.image.load(sneaker.image)
    }

    // getItemCount devuelve el número de elementos en la lista
    override fun getItemCount(): Int = sneakers.size
}
