package com.app.chattyai.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.viewpager.widget.PagerAdapter
import com.app.chattyai.R
import com.app.chattyai.fragments.LoginFragment
import com.app.chattyai.interfaces.PageAdapterListner
import com.app.chattyai.models.Pagerplanmodel

class PackageViewPagerAdapter(
    private val activity: Activity,
    private val arrayList: ArrayList<Pagerplanmodel>,
   val pageAdapterListner: PageAdapterListner,
) : PagerAdapter() {

    private val layoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        // return the number of images
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as LinearLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = layoutInflater.inflate(R.layout.packagepageritem, container, false)

        // referencing the image view from the item.xml file
        val tvtext1 = itemView.findViewById<TextView>(R.id.tvmaintext)
        val tvtokenamount = itemView.findViewById<TextView>(R.id.tvtokenamount)
        val tvimagegennamount = itemView.findViewById<TextView>(R.id.tvimagegennamount)
        val tvilimitamount = itemView.findViewById<TextView>(R.id.tvilimitamount)
        val tvplanprice = itemView.findViewById<TextView>(R.id.tvplanprice)
        val tvduration = itemView.findViewById<TextView>(R.id.tvduration)
        val linmain = itemView.findViewById<LinearLayout>(R.id.linmain)
        val tvtokentext = itemView.findViewById<TextView>(R.id.tvtokentext)
        val tvimgext = itemView.findViewById<TextView>(R.id.tvimgext)
        val tvtranscriptionext = itemView.findViewById<TextView>(R.id.tvtranscriptionext)

        val linbuyplan = itemView.findViewById<LinearLayout>(R.id.linbuyplan)
        linbuyplan.setOnClickListener{
            pageAdapterListner.passclick()
        }

        // setting the image in the imageView
        tvtext1.text = arrayList[position].planname
        tvtokenamount.text = arrayList[position].tokenamount
        tvimagegennamount.text = arrayList[position].imagegenerationamout
        tvilimitamount.text = arrayList[position].transscriptionlimitplan
        tvplanprice.text = arrayList[position].amount
        tvduration.text = arrayList[position].subscriptionduration
        tvtokentext.text = arrayList[position].tokentext
        tvimgext.text = arrayList[position].imagetext
        tvtranscriptionext.text = arrayList[position].transcriptiontext
        if (position==1){
            linmain.setBackgroundDrawable(activity.getDrawable(R.drawable.lessroundeddrawable))
        }else if (position==2){
            linmain.setBackgroundDrawable(activity.getDrawable(R.drawable.greenroundcodrawable))

        }else{
            linmain.setBackgroundDrawable(activity.getDrawable(R.drawable.yellowroundcodrawable))

        }
        // Adding the View
        container.addView(itemView)


        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}