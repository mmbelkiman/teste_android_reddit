package com.fastnews.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastnews.R
import com.fastnews.mechanism.VerifyNetworkInfo
import com.fastnews.service.model.PostData
import com.fastnews.service.model.Preview
import com.fastnews.service.model.PreviewImage
import com.fastnews.service.model.realm.PostResponseEntity
import com.fastnews.ui.detail.DetailFragment.Companion.KEY_POST
import com.fastnews.viewmodel.PostViewModel
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment : Fragment() {
    //region fields
    private var postsLastAfterKey = ""
    private val viewModel: PostViewModel by lazy {
        ViewModelProviders.of(this).get(PostViewModel::class.java)
    }
    private lateinit var adapter: TimelineAdapter
//endregion

    //region superclass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildActionBar()
        buildTimeline()
        verifyConnectionState()
    }
    //endregion

    private fun buildActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false) // disable the button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false) // remove the left caret
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.app_name)
    }

    private fun buildTimeline() {
        adapter = TimelineAdapter { it, imageView ->
            onClickItem(it, imageView)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        timeline_rv.layoutManager = linearLayoutManager
        timeline_rv.itemAnimator = DefaultItemAnimator()
        timeline_rv.adapter = adapter
        timeline_srl.setOnRefreshListener { onRefreshListener() }
        timeline_rv.addOnScrollListener(object :
            TimelineScrollListener(linearLayoutManager) {

            override fun isLoading(): Boolean {
                return timeline_srl.isRefreshing
            }

            override fun loadMoreItems() {
                timeline_srl.isRefreshing = true
                timeline_rv.post {
                    context.let {
                        if (VerifyNetworkInfo.isConnected(it!!)) {
                            fetchTimelineMoreContent()
                        } else {
                            timeline_srl.isRefreshing = false
                        }
                    }
                }
            }
        })
    }

    private fun verifyConnectionState() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                hideNoConnectionState()
                showProgress()
                fetchTimelineTop()
            } else {
                hideProgress()
                showNoConnectionState()

                state_without_conn_timeline.setOnClickListener {
                    verifyConnectionState()
                }
            }
        }
    }

    private fun fetchTimelineMoreContent() {
        viewModel.getPosts(postsLastAfterKey)
            .observe(viewLifecycleOwner, { posts ->
                posts.let {
                    adapter.addData(posts)
                    timeline_srl.isRefreshing = false
                    Toast.makeText(context, getString(R.string.toast_new_data), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun fetchTimelineTop() {
        viewModel.getPosts(postsLastAfterKey)
            .observe(viewLifecycleOwner, { posts ->
                posts.let {
                    if (posts.size > 0) {
                        postsLastAfterKey = (posts[posts.size - 1]).name
                        adapter.setData(posts)
                        hideProgress()
                        showPosts()
                        timeline_srl.isRefreshing = false
                    }
                }
            })
    }

    private fun showPosts() {
        timeline_rv.visibility = View.VISIBLE
    }

    private fun showProgress() {
        state_progress_timeline.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        state_progress_timeline.visibility = View.GONE
    }

    private fun showNoConnectionState() {
        state_without_conn_timeline.visibility = View.VISIBLE

        adapter.setData(getRealmData())
        hideProgress()
        showPosts()
        timeline_srl.isRefreshing = false
    }

    private fun getRealmData(): MutableList<PostData> {
        val realm = Realm.getDefaultInstance()
        val query: RealmQuery<PostResponseEntity> = realm.where(PostResponseEntity::class.java)
        val results: RealmResults<PostResponseEntity> = query.findAll()
        val posts: MutableList<PostData> = mutableListOf()
        val listPreviewImages: List<PreviewImage> = listOf()
        val preview = Preview(listPreviewImages)

        results.forEach {
            val postData = PostData(
                it.id,
                it.author,
                it.thumbnail,
                it.name,
                it.num_comments,
                it.score,
                it.title,
                it.created_utc,
                it.url,
                preview
            )

            posts.add(postData)
        }
        return posts
    }

    private fun hideNoConnectionState() {
        state_without_conn_timeline.visibility = View.GONE
    }

    //regions listeners
    private fun onRefreshListener() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                postsLastAfterKey = ""
                fetchTimelineTop()
                hideNoConnectionState()
            } else {
                timeline_srl.isRefreshing = false
            }
        }
    }

    private fun onClickItem(postData: PostData, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageView to "thumbnail"
        )
        val bundle = Bundle()
        bundle.putParcelable(KEY_POST, postData)
        findNavController().navigate(R.id.action_timeline_to_detail, bundle, null, extras)
    }
    //endregion
}