package net.apptimystic.appoflife.feature.todo

import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.apptimystic.appoflife.data.header.Header
import net.apptimystic.appoflife.data.todo.Todo
import net.apptimystic.appoflife.feature.todo.recyclerview.HeaderViewHolder
import net.apptimystic.appoflife.feature.todo.recyclerview.RVTodoMVP
import net.apptimystic.appoflife.feature.todo.recyclerview.TodoViewHolder
import java.lang.ref.WeakReference
import javax.inject.Inject

class TodoPresenter @Inject constructor(var model: TodoActivityMVP.Model) : TodoActivityMVP.Presenter {

    var disposable: Disposable? = null
    override lateinit var view: WeakReference<TodoActivityMVP.View>
    var viewModel: TodoViewModel? = null
    var adapterList: MutableList<Any> = mutableListOf()

    override fun loadData() {
        disposable = model
                .result()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> onDataLoaded(result) },
                        {error -> view.get()?.showSnackBar(error.localizedMessage)}
                )
    }

    private fun onDataLoaded(result: TodoViewModel) {
        adapterList.clear()
        viewModel = result
        view.get()?.updateData(result)
        val routineList: MutableList<Any> = mutableListOf()
        viewModel?.morningTodos?.let {
            routineList.add(Header("Morning Routine"))
            routineList.addAll(it as List<Any>)
        }
        viewModel?.morningTodos?.let {
            routineList.add(Header("Evening Routine"))
            routineList.addAll(it as List<Any>)
        }
        adapterList = routineList
    }

    override fun rxUnsubscribe() {
        disposable?.dispose()
    }

    // Routine RecyclerView

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when(viewHolder) {
            is RVTodoMVP.HeaderView -> bindHeaderView(viewHolder, position)
            is RVTodoMVP.TodoView -> bindTodoView(viewHolder, position)
        }
    }

    private fun bindHeaderView(viewHolder: RVTodoMVP.HeaderView, position: Int) {
        val header = adapterList[position] as Header
        viewHolder.setLabel(header.label)
    }

    private fun bindTodoView(viewHolder: RVTodoMVP.TodoView, position: Int) {
        val todo = adapterList[position] as Todo
        viewHolder.setDesc(todo.desc)
    }

    override fun getItemCount(): Int = adapterList.size

    override fun getItemType(position: Int): Int {
        return when(adapterList[position]) {
            is Todo -> TodoViewHolder.viewType
            is Header -> HeaderViewHolder.viewType
            else -> throw IllegalStateException("can't handle provided data")
        }
    }
}