package com.yun.simpledaily.base

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerAdapter {
    interface OnItemClickListener<T, V> {
        fun onItemClick(item: T, view: V)
    }

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_TOP = 1
        const val TYPE_BUTTON = 2
        const val TYPE_HOLDER = 3
        const val TYPE_ADS = 4
    }

    abstract class Create<ITEM : Item, B : ViewDataBinding>(
        @LayoutRes private val layoutResId: Int,
        @LayoutRes private val buttonLayoutResId: Int? = null,
        @LayoutRes private val adsLayoutResId: Int? = null,
        @LayoutRes private val topLyoutResId: Int? = null,
        @LayoutRes private val holdLayoutResId: Int? = null,
        private val bindingVariableId: Int,
        private val bindingListener: Int,
        private val bindingViewModel: ViewModel? = null
    ) : RecyclerView.Adapter<BaseViewHolder<B>>(), OnItemClickListener<ITEM, View>, Filterable {

        init {
            setHasStableIds(true)
        }

        var mItems = ListLiveData<ITEM>()

        override fun getFilter(): Filter? = null

        override fun getItemId(position: Int): Long {
            return mItems.get(position).id.toLong()
        }

        fun replaceAll(items: List<ITEM>, isClear: Boolean = true) {
            if (isClear) {
                mItems.clear(false)
            }
            mItems.addAll(items)
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mItems.sizes()
                }

                override fun getNewListSize(): Int {
                    return items.size
                }

                override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return mItems.get(oldItemPosition).id === items[newItemPosition].id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val newProduct = items[newItemPosition]
                    val oldProduct = mItems.get(oldItemPosition)
                    return newProduct.id === oldProduct.id
                }
            })
            result.dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {
            return mItems.get(position).viewType
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
            val resId: Int? = when (viewType) {
                TYPE_BUTTON -> {
                    buttonLayoutResId
                }
                TYPE_TOP -> {
                    topLyoutResId
                }
                TYPE_HOLDER -> {
                    holdLayoutResId
                }
                TYPE_ADS -> {
                    adsLayoutResId
                }
                else -> {
                    layoutResId
                }
            }

            checkNotNull(resId) { "type layout resource not found" }

            val binding: B = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                resId,
                parent,
                false
            )
            binding.setVariable(bindingListener, this)
//            when (bindingViewModel) {
//                is HomeViewModel -> binding.setVariable(BR.home, bindingViewModel)
//                is EncyclopediaViewModel -> binding.setVariable(BR.encyclopedia, bindingViewModel)
//                is CalendarViewModel -> binding.setVariable(BR.calendar, bindingViewModel)
//            }

            return object : BaseViewHolder<B>(binding) {}
        }

        override fun onBindViewHolder(holder: BaseViewHolder<B>, position: Int) {
            holder.binding.setVariable(bindingVariableId, mItems.get(position))
            holder.binding.executePendingBindings()
        }

        override fun getItemCount() = mItems.sizes()

        fun isHolder(position: Int): Boolean {
            return mItems.get(position).viewType == TYPE_HOLDER
        }

        fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
            if (holdLayoutResId != null) {
                val lastIndex =
                    if (position < mItems.sizes()) position else mItems.sizes() - 1
                for (index in lastIndex downTo 0) {
                    val model = mItems.get(index)
                    if (model.viewType == TYPE_HOLDER) {
                        return LayoutInflater.from(list.context)
                            .inflate(holdLayoutResId, list, false)
                    }
                }
            }
            return null
        }
    }

    abstract class BaseViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(
        binding.root
    )

}

abstract class Item : BaseObservable() {
    abstract var id: Int
    abstract var viewType: Int
}

@BindingAdapter("replaceAll")
fun RecyclerView.replace(list: List<Item>?) {
    list?.let {
        (adapter as? BaseRecyclerAdapter.Create<Item, *>)?.run {
            replaceAll(it)
        }
    }
}

class SectionItemDecoration(sectionCallback: SectionCallback) :
    RecyclerView.ItemDecoration() {
    private val sectionCallback: SectionCallback = sectionCallback

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val currentHeader: View =
            sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return
        fixLayoutSize(parent, currentHeader)


        val contactPoint = currentHeader.bottom
        val childInContact: View = (getChildInContact(parent, contactPoint) ?: return)

        val childAdapterPosition = parent.getChildAdapterPosition(childInContact)
        if (childAdapterPosition == -1) {
            return
        }
        if (sectionCallback.isSection(childAdapterPosition)) {
            moveHeader(c, currentHeader, childInContact)
            return
        }
        drawHeader(c, currentHeader)
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }


    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )
        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {
        fun isSection(position: Int): Boolean
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View?
    }

}