package com.sorcerer.sorcery.iconpack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialize.util.UIUtils
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconTabFragment
import com.sorcerer.sorcery.iconpack.network.avos.AvosStatisticManager
import com.sorcerer.sorcery.iconpack.ui.activities.WelcomeActivity
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity
import com.sorcerer.sorcery.iconpack.ui.anim.SearchTransitioner
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar
import com.sorcerer.sorcery.iconpack.utils.PackageUtil
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by Sorcerer on 2016/6/1 0001.
 *
 *
 * MainActivity
 * The first activity with drawer and icon viewpager.
 */
class MainActivity : BaseActivity() {

    var mSearchToolbar: ExposedSearchToolbar? = null

    var mAppBarLayout: AppBarLayout? = null

    private var mDrawer: Drawer? = null

    var isCustomPicker = false
        private set

    private var mNavigator: Navigator? = null
    private var mSearchTransitioner: SearchTransitioner? = null

    private var mIconTabFragment: IconTabFragment? = null

    override fun hookBeforeSetContentView() {
        super.hookBeforeSetContentView()

        if (ENABLE_GUIDE && !mPrefs.userGuideShowed().value) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            overridePendingTransition(0, 0)
            this.finish()
        }
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) {
            AvosStatisticManager(this).post()
        }

        mSearchToolbar = findViewById<ExposedSearchToolbar>(R.id.exposedSearchToolbar)
        mAppBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout_main)

        window.setBackgroundDrawable(null)

        isCustomPicker = isCustomPicker(intent)

        setSupportActionBar(mSearchToolbar)

        mNavigator = Navigator(this)

        initDrawer()

        mSearchToolbar!!.setOnClickListener {
            mAppBarLayout!!.setExpanded(true, true)
            mAppBarLayout!!.post { mSearchTransitioner!!.transitionToSearch() }
        }
        mSearchToolbar!!.setOnLongClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
            mAppBarLayout!!.setExpanded(true, true)
            mAppBarLayout!!.post { mSearchTransitioner!!.transitionToSearch() }
            true
        }
        mSearchToolbar!!.title = "Sorcery Icons"

        if (isCustomPicker) {
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.title = getString(R.string.select_an_icon)
            }
        } else {
        }
    }

    private fun initDrawer() {

        mDrawer = DrawerBuilder()
                .withSliderBackgroundColor(ResourceUtil.getAttrColor(this, R.attr.colorCard))
                .withCloseOnClick(false)
                .withToolbar(mSearchToolbar!!)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerNavigationListener {
                    openDrawer()
                    true
                }
                .withActivity(mActivity)
                .build()

        val textColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorPrimary)
        val subTextColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorSecondary)
        val iconColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorSecondary)
        val iconSize = 48

        mDrawer!!.addItems(
                PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("apply")
                        .withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_palette)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_apply),
                PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("request")
                        .withIcon(IconicsDrawable(this,
                                GoogleMaterial.Icon.gmd_playlist_add_check)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.request),
                PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("settings")
                        .withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_settings),
                PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("help")
                        .withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_help)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_help)
                        .withDescription(R.string.nav_item_help_description)
                        .withDescriptionTextColor(subTextColor)
        )

        if (PackageUtil.isAlipayInstalled(this)) {
            mDrawer!!.addItem(PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("donate")
                    .withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_atm)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_donate))
        }
        mDrawer!!.addItem(DividerDrawerItem())
        mDrawer!!.addItem(PrimaryDrawerItem()
                .withSetSelected(false)
                .withSelectable(false)
                .withTag("suggest")
                .withTextColor(subTextColor)
                .withName(R.string.nav_item_feedback))

        if (BuildConfig.DEBUG) {
            mDrawer!!.addItemAtPosition(PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("custom")
                    .withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_create)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_custom_workshop), 1)
            mDrawer!!.addItem(PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("test")
                    .withTextColor(subTextColor)
                    .withName("Test"))
        }

        mDrawer!!.setOnDrawerItemClickListener { _, _, drawerItem ->
            when (drawerItem.tag as String) {
                "apply" -> mNavigator!!.toAppleActivity()
                "settings" -> mNavigator!!.toSettingsActivity()
                "help" -> mNavigator!!.toHelpMarkdownActivity()
                "donate" -> mNavigator!!.toDonateActivity()
                "request" -> mNavigator!!.toIconRequest()
                "suggest" -> showFeedbackOptionPanel()
                "test" -> mNavigator!!.toTestActivity()
            }
            Handler().post { this.closeDrawer() }
            false
        }

        val drawerRecyclerView = mDrawer!!.recyclerView
        drawerRecyclerView.isVerticalScrollBarEnabled = false
        drawerRecyclerView.isHorizontalScrollBarEnabled = false

        val viewTreeObserver = drawerRecyclerView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    drawerRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val dimensions = BitmapFactory.Options()
                    dimensions.inJustDecodeBounds = true
                    BitmapFactory.decodeResource(resources, R.drawable.drawer_head_simple,
                            dimensions)
                    var height = dimensions.outHeight
                    val width = dimensions.outWidth

                    height = Math.ceil(
                            1.0 * height.toDouble() * drawerRecyclerView.width.toDouble() / width).toInt()

                    val view = View.inflate(this@MainActivity, R.layout.layout_drawer_head, null)
                    val image = view.findViewById<ImageView>(R.id.imageView_drawer_head)

                    val drawable = ResourceUtil
                            .getDrawable(this@MainActivity, R.drawable.drawer_head_simple)
                    image.setImageDrawable(drawable)
                    var imageLayoutParams: LayoutParams? = image.layoutParams
                    if (imageLayoutParams == null) {
                        imageLayoutParams = LayoutParams(MATCH_PARENT, height)
                    } else {
                        imageLayoutParams.height = height
                    }
                    image.layoutParams = imageLayoutParams


                    if (mPrefs.nightMode()) {
                        image.setImageDrawable(ColorDrawable(
                                ResourceUtil.getAttrColor(mContext, R.attr.colorCard)))
                    }

                    val bottomSpace = view.findViewById<View>(R.id.view_drawer_head_space_bottom)
                    var bottomSpaceLayoutParams: LayoutParams? = bottomSpace.layoutParams
                    val bottomPadding = Math.max(0,
                            UIUtils.convertDpToPixel(178f, this@MainActivity).toInt() - height
                    )
                    if (bottomSpaceLayoutParams == null) {
                        bottomSpaceLayoutParams = LayoutParams(MATCH_PARENT, bottomPadding)
                    } else {
                        bottomSpaceLayoutParams.height = bottomPadding
                    }
                    bottomSpace.layoutParams = bottomSpaceLayoutParams

                    mDrawer!!.setHeader(view, false, false)

                    mPrefs.firstTimeLaunch().asObservable()
                            .filter { firstTime -> firstTime }
                            .observeOn(Schedulers.newThread())
                            .delay(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                mDrawer!!.openDrawer()
                                mPrefs.firstTimeLaunch().set(false)
                            }, { Timber.e(it) }
                            )
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()

        if (mIconTabFragment == null) {
            mIconTabFragment = IconTabFragment.newInstance(isCustomPicker)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.linearLayout_content_main, mIconTabFragment)
                    .commitNow()
            mSearchTransitioner = SearchTransitioner(this,
                    Navigator(this),
                    mIconTabFragment!!.tabLayout,
                    mIconTabFragment!!.viewPager,
                    mSearchToolbar,
                    ViewFader())
        }

        window.decorView.postDelayed({
            if (mSearchTransitioner != null) {
                mSearchTransitioner!!.onActivityResumed()
            }
        }, 100)
    }

    override fun onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed()
        }
    }

    private fun openDrawer() {
        if (mDrawer != null) {
            mDrawer!!.openDrawer()
        }
    }

    private fun closeDrawer(): Boolean {
        if (mDrawer != null && mDrawer!!.isDrawerOpen) {
            mDrawer!!.closeDrawer()
            return true
        }
        return false
    }

    private fun showFeedbackOptionPanel() {
        if (!Navigator.toMail(this@MainActivity,
                "feedback@sorcererxw.com",
                "Sorcery Icons feedback",
                "")) {
            Snackbar.make(mDrawer!!.drawerLayout,
                    R.string.feedback_need_mailbox,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_install
                    ) {
                        Navigator.toAppMarketSearchResult(this,
                                ResourceUtil.getString(this, R.string.market_search_mail))
                    }
                    .show()
        }
//        var builder = BottomSheetDialogBuilder(this)
//        builder.build().show()
    }

    fun onReturnCustomPickerRes(res: Int) {
        val intent = Intent()
        var bitmap: Bitmap? = null

        try {
            bitmap = BitmapFactory.decodeResource(mContext.resources, res)
        } catch (e: Exception) {
            Timber.e(e)
        }

        if (bitmap != null) {
            intent.putExtra("icon", bitmap)
            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", res)
            val bmUri = "android.resource://" + mContext.packageName + "/" + res.toString()
            intent.data = Uri.parse(bmUri)
            setResult(Activity.RESULT_OK, intent)
        } else {
            setResult(Activity.RESULT_CANCELED, intent)
        }
        finish()
    }

    companion object {

        val REQUEST_ICON_DIALOG = 100

        private val ENABLE_GUIDE = false

        private fun isCustomPicker(intent: Intent): Boolean {
            return intent.hasCategory("com.novalauncher.category.CUSTOM_ICON_PICKER")
        }
    }
}