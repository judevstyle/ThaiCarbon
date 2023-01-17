package com.taitos.testpjk.view.working

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.ref.WeakReference


class TabAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()


    private val instantiatedFragments: SparseArray<WeakReference<Fragment>> =
        SparseArray<WeakReference<Fragment>>()


    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment =
            super.instantiateItem(container!!, position) as Fragment
        instantiatedFragments.put(
            position,
            WeakReference(fragment)
        )
        return fragment
    }

    override  fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        instantiatedFragments.remove(position)
        super.destroyItem(container!!, position, `object`!!)
    }

    fun getFragment(position: Int): Fragment? {
        val wr =
            instantiatedFragments[position]
        return wr?.get()
    }
    /**
     * If you want to only show icons, return null from this method.
     * @param position
     * @return
     */



    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}