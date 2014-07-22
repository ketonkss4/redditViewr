package Cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.redditviewr.app.R;

/**
 * Created by Administrator on 7/21/2014.
 */
public class CardsLayout extends RelativeLayout implements OnGlobalLayoutListener {


    public CardsLayout(Context context) {
        super(context);
        initLayoutObserver();
    }

    public CardsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayoutObserver();
    }

   private void initLayoutObserver(){
        //force vertical orientation and add observer
       getViewTreeObserver().addOnGlobalLayoutListener(this);
   }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        final int heightPx = getContext().getResources().getDisplayMetrics().heightPixels;

        boolean inversed = false;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            int[] location = new int[2];

            child.getLocationOnScreen(location);

            if (location[1] > heightPx){
                break;
            }

        if (!inversed){
            child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_left));
        }else{
            child.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_up_right));
        }
            inversed = !inversed;
        }
    }
}
