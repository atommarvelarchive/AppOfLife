package net.apptimystic.appoflife.feature.checklist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_tasks.*
import net.apptimystic.appoflife.R
import net.apptimystic.appoflife.core.App
import net.apptimystic.appoflife.feature.checklist.recyclerview.ChecklistRVAdapter
import net.apptimystic.appoflife.feature.checklist.recyclerview.SwipeTouchHelper
import net.apptimystic.appoflife.ktx.snack
import java.lang.ref.WeakReference
import javax.inject.Inject


class ChecklistView : AppCompatActivity(), ChecklistMVP.View {

    @Inject lateinit var presenter: ChecklistMVP.Presenter
    @Inject lateinit var rvAdapter: ChecklistRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        (application as App).component?.inject(this)
        setupRV()
        supportActionBar?.title = intent.getStringExtra("name")
    }

    fun setupRV() {
        rvTasks.layoutManager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        rvTasks.addItemDecoration(decoration)
        val touchHelper = ItemTouchHelper(SwipeTouchHelper(presenter))
        touchHelper.attachToRecyclerView(rvTasks)
        rvTasks.adapter = rvAdapter
    }

    override fun onResume() {
        super.onResume()
        presenter.view = WeakReference(this)
        presenter.loadChecklist(intent.getStringExtra("id"))
    }

    override fun onPause() {
        super.onPause()
        presenter.rxUnsubscribe()
    }

    override fun updateData(viewModel: ChecklistViewModel) {
        rvAdapter.notifyDataSetChanged()
    }

    override fun showSnackBar(msg: String) {
        rvTasks.snack(msg)
    }
}
