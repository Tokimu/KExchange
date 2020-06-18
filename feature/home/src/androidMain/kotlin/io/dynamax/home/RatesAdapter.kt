package io.dynamax.home

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.emoji.text.EmojiCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import io.dynamax.home.databinding.ItemRatesBinding
import io.dynamax.model.CurrencyRate

class RatesAdapter(
        private val onItemClick: (CurrencyRate) -> Unit,
        onCurrencyAmountChanged: (Double) -> Unit
) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private val currencyAmountEditTextWatcher = CurrencyAmountTextWatcher(onCurrencyAmountChanged)

    private val diffCallback: ItemCallback<CurrencyRate> = object : ItemCallback<CurrencyRate>() {
        override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
            return if (oldItem.value == newItem.value) true
            else oldItem.value == oldItem.rate && newItem.value == newItem.rate
        }
    }

    private val mDiffer = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding = ItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = RatesViewHolder(binding)

        viewHolder.itemView.apply { setOnClickListener { requestFocus() } }
        viewHolder.apply {
            amountEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (adapterPosition != 0) onItemClick(mDiffer.currentList[adapterPosition])
                    amountEditText.addTextChangedListener(currencyAmountEditTextWatcher)
                    amountEditText.setSelection(viewHolder.amountEditText.text.length)
                } else {
                    amountEditText.removeTextChangedListener(currencyAmountEditTextWatcher)
                }
                amountEditText.setOnClickListener {
                    val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                }
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    fun setRates(rates: List<CurrencyRate>) {
        mDiffer.submitList(rates)
    }

    inner class RatesViewHolder(private val binding: ItemRatesBinding) : RecyclerView.ViewHolder(binding.root) {

        val amountEditText = binding.amountEdittext

        fun bind(item: CurrencyRate) {
            binding.apply {
                amountEdittext.setText(item.value.toString())
                currency.text = item.name.toString()
                name.text = item.name.fullName
                flag.text = EmojiCompat.get().process(item.name.emojiCode)
            }
        }
    }

    inner class CurrencyAmountTextWatcher(private val onCurrencyAmountChanged: (Double) -> Unit) : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(currentAmountValueText: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            val value = editable.toString()
            if (value.startsWith(".")) value.replace(".", "0.")
            onCurrencyAmountChanged(value.ifEmpty { "0" }.toDouble())
        }
    }
}
