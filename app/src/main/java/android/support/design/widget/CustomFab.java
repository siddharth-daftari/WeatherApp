package android.support.design.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by apoorv.mehta on 11/12/17.
 */
@CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class CustomFab extends AppCompatImageButton {

    public CustomFab(Context context) {
        super(context);
    }

    public CustomFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
