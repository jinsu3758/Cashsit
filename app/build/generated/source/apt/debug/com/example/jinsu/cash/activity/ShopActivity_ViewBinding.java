// Generated code from Butter Knife. Do not modify!
package com.example.jinsu.cash.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.jinsu.cash.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopActivity_ViewBinding implements Unbinder {
  private ShopActivity target;

  @UiThread
  public ShopActivity_ViewBinding(ShopActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShopActivity_ViewBinding(ShopActivity target, View source) {
    this.target = target;

    target.shopImCoffee = Utils.findRequiredViewAsType(source, R.id.shop_im_coffee, "field 'shopImCoffee'", ImageView.class);
    target.shopLayoutCoffee = Utils.findRequiredViewAsType(source, R.id.shop_layout_coffee, "field 'shopLayoutCoffee'", LinearLayout.class);
    target.shopImBakery = Utils.findRequiredViewAsType(source, R.id.shop_im_bakery, "field 'shopImBakery'", ImageView.class);
    target.shopLayoutBakery = Utils.findRequiredViewAsType(source, R.id.shop_layout_bakery, "field 'shopLayoutBakery'", LinearLayout.class);
    target.shopImDesert = Utils.findRequiredViewAsType(source, R.id.shop_im_desert, "field 'shopImDesert'", ImageView.class);
    target.shopLayoutDesert = Utils.findRequiredViewAsType(source, R.id.shop_layout_desert, "field 'shopLayoutDesert'", LinearLayout.class);
    target.shopImMore = Utils.findRequiredViewAsType(source, R.id.shop_im_more, "field 'shopImMore'", ImageView.class);
    target.shopLayoutMore = Utils.findRequiredViewAsType(source, R.id.shop_layout_more, "field 'shopLayoutMore'", LinearLayout.class);
    target.shopRecycler = Utils.findRequiredViewAsType(source, R.id.shop_recycler, "field 'shopRecycler'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShopActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.shopImCoffee = null;
    target.shopLayoutCoffee = null;
    target.shopImBakery = null;
    target.shopLayoutBakery = null;
    target.shopImDesert = null;
    target.shopLayoutDesert = null;
    target.shopImMore = null;
    target.shopLayoutMore = null;
    target.shopRecycler = null;
  }
}
