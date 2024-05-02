# DixSurDixGeoffrion

## How to change an image view source

myImgView.setBackgroundResource(R.drawable.monkey);

*** With new android API 22 getResources().getDrawable() is now deprecated. This is an example how to use now:

myImgView.setImageDrawable(getResources().getDrawable(R.drawable.monkey, getApplicationContext().getTheme()));

and how to validate for old API versions:

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     myImgView.setImageDrawable(getResources().getDrawable(R.drawable.monkey, getApplicationContext().getTheme()));
   } else {
     myImgView.setImageDrawable(getResources().getDrawable(R.drawable.monkey));
}