// Generated code from Butter Knife. Do not modify!
package com.example.jinsu.cash.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.jinsu.cash.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ShopAdapter.ViewHolder target;

  @UiThread
  public ShopAdapter$ViewHolder_ViewBinding(ShopAdapter.ViewHolder target, View source) {
    this.target = target;

    target.shopImItem = Utils.findRequiredViewAsType(source, R.id.shop_im_item, "field 'shopImItem'", ImageView.class);
    target.shopImDot = Utils.findRequiredViewAsType(source, R.id.shop_im_dot, "field 'shopImDot'", ImageView.class);
    target.shopTxtBrand = Utils.findRequiredViewAsType(source, R.id.shop_txt_brand, "field 'shopTxtBrand'", TextView.class);
    target.shopTxtName = Utils.findRequiredViewAsType(source, R.id.shop_txt_name, "field 'shopTxtName'", TextView.class);
    target.shopTxtPrice = Utils.findRequiredViewAsType(source, R.id.shop_txt_price, "field 'shopTxtPrice'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShopAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.shopImItem = null;
    target.shopImDot = null;
    target.shopTxtBrand = null;
    target.shopTxtName = null;
    target.shopTxtPrice = null;
  }
}
