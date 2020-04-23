package grit.android.hangtheman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

public class ImageHandler {
    ImageView progressImageView;
    Bitmap hangmanSprite;
    View view;

    public ImageHandler(View view){
        this.view = view;

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inScaled = false;


        progressImageView = view.findViewById(R.id.progressImageView);
        hangmanSprite = BitmapFactory.decodeResource(view.getResources(), R.drawable.hangman_sprite, bitmapFactoryOptions);

        setImage(0);

    }

    public void setImage(int mistakes){
        if (mistakes > 6) throw new IllegalArgumentException("Mistakes cannot be more than 6");

        progressImageView.setImageBitmap(Bitmap.createBitmap(hangmanSprite, mistakes * 75, 0, 75, 200));
    }
}
