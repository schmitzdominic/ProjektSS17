package de.projektss17.bonpix.daten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.projektss17.bonpix.A_Max_Bon_Pic;
import de.projektss17.bonpix.A_OCR_Manuell;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by SemperFi on 21.05.2017.
 */

public class C_Bon_Anzeigen_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private C_Bon bon;     // Zwischenspeicher für die Bons
    public int count;     // Count für die Inhalte der RecyclerView
    public int counter = 0;
    private ArrayList<C_Artikel> artikel = new ArrayList<>();


    public C_Bon_Anzeigen_Adapter(C_Bon bon){
        this.bon = bon;
        artikel = bon.getArticles();
        count = artikel.size();
        //Hier muss das Array eigebtlich um 1 Hochgezählt werden jedoch stürzt dann die App ab
        //count += 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }
        else {
            return 1;
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {

        public TextView ladenName, adresse, datum, artikel,garantie,gesbetrag;
        public ImageView kassenzettel;
        public Uri image;
        public View v;

        public ViewHolderHeader(View view){
            super(view);
            this.v = view;
            kassenzettel  = (ImageView) view.findViewById(R.id.bon_anzeigen_picture);
            ladenName = (TextView) view.findViewById(R.id.bon_anzeigen_ladenname);
            adresse = (TextView) view.findViewById(R.id.bon_anzeigen_adresse);
            datum = (TextView) view.findViewById(R.id.bon_anzeigen_datum);
            artikel = (TextView) view.findViewById(R.id.bon_anzeigen_artikel);
            gesbetrag = (TextView) view.findViewById(R.id.bon_anzeigen_gesbetrag);
            garantie = (TextView) view.findViewById(R.id.bon_anzeigen_garantie);

            kassenzettel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(image != null){
                        Intent intent = new Intent(v.getContext(), A_Max_Bon_Pic.class);
                        intent.putExtra("imageUri", image.toString());
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }

        /**
         *
         * @param uri
         */
        public void setImage(Uri uri){
            this.image = uri;
        }

        /**
         * Bekommt die Uri aus einem Bitmap zurück
         * @param inImage Bitmap
         * @return Uri des Bitmap
         */
        public Uri getImageUri(Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(v.getContext().getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

    }


    public class ViewHolderBottom extends RecyclerView.ViewHolder{
        public TextView artikel, preis;

        public ViewHolderBottom (View view) {
            super(view);
            artikel = (TextView) view.findViewById(R.id.bon_anzeigen_artikel);
            preis = (TextView) view.findViewById(R.id.bon_anzeigen_artikel_preis);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bon_anzeigen_header_content, parent, false);
                return new ViewHolderHeader(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bon_anzeigen_bottom_content, parent, false);
                return new ViewHolderBottom(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case 0:
                double gesBetrag = 0;
                for(C_Artikel article : bon.getArticles()){
                    gesBetrag += article.getPrice();
                }

                gesBetrag = Math.round(gesBetrag * 100) / 100.00;
                DecimalFormat df = new DecimalFormat("#0.00");
                ViewHolderHeader holderHeader = (ViewHolderHeader)holder;

                if (!bon.getPath().equals("PFAD")) {
                    File image = new File(bon.getPath());
                    Log.e("###BON GETPATH", "" + bon.getPath());
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

                    holderHeader.kassenzettel.setImageBitmap(bitmap);
                    holderHeader.setImage(holderHeader.getImageUri(bitmap));
                }
                holderHeader.ladenName.setText(bon.getShopName());
                holderHeader.adresse.setText(bon.getAdress());
                holderHeader.datum.setText(bon.getDate());
                holderHeader.garantie.setText(bon.getGuaranteeEnd());
                holderHeader.gesbetrag.setText(df.format(gesBetrag)+" €");
                counter++;
            case 1:

                try{
                    ViewHolderBottom holderBottom = (ViewHolderBottom)holder;
                    holderBottom.artikel.setText(artikel.get(getArticleIndex()).getName());
                    holderBottom.preis.setText(Double.toString(artikel.get(getArticleIndex()).getPrice()) +" €");
                } catch(ClassCastException e){

            }
                //counter++;

        }
    }

    @Override
    public int getItemCount() {

        return count;
    }

    //TODO: Überprüfen!
    public int getArticleIndex(){
        if (counter > count){
            counter = 1;
            return counter -1;
        }
        else {
            return counter -1;
        }
    }
}