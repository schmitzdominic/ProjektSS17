package de.projektss17.bonpix.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.projektss17.bonpix.A_Max_Bon_Pic;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.daten.C_Bon;

public class C_Adapter_Bon_Anzeigen extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private C_Bon bon;
    public int count;
    public int counter = 0;
    public int recycled = 0;
    private ArrayList<C_Artikel> artikel = new ArrayList<>();

    public C_Adapter_Bon_Anzeigen(C_Bon bon){
        this.bon = bon;
        artikel = bon.getArticles();
        count = artikel.size() + 1;
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

        public TextView ladenName, adresse, adresseTitle, datum, artikel, garantie, garantieTitle, gesbetrag;
        public ImageView kassenzettel;
        public String image;
        public View v;

        public ViewHolderHeader(View view){
            super(view);
            this.v = view;
            kassenzettel  = (ImageView) view.findViewById(R.id.bon_anzeigen_picture);
            ladenName = (TextView) view.findViewById(R.id.bon_anzeigen_ladenname);
            adresse = (TextView) view.findViewById(R.id.bon_anzeigen_adresse);
            adresseTitle = (TextView) view.findViewById(R.id.bon_anzeigen_adresse_title);
            datum = (TextView) view.findViewById(R.id.bon_anzeigen_datum);
            artikel = (TextView) view.findViewById(R.id.bon_anzeigen_artikel);
            gesbetrag = (TextView) view.findViewById(R.id.bon_anzeigen_gesbetrag);
            garantie = (TextView) view.findViewById(R.id.bon_anzeigen_garantie);
            garantieTitle = (TextView) view.findViewById(R.id.bon_anzeigen_garantie_title);

            kassenzettel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(image != null){
                        Intent intent = new Intent(v.getContext(), A_Max_Bon_Pic.class);
                        intent.putExtra("imageUri", image);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public class ViewHolderBottom extends RecyclerView.ViewHolder{
        public TextView artikel, preis;
        public View v;

        public ViewHolderBottom (View view) {
            super(view);
            this.v = view;
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
                ViewHolderHeader holderHeader = (ViewHolderHeader)holder;

                if(bon.getPath() != null && bon.getPath().contains(".")){
                    if (bon.getPath().split("\\.")[1].equals("jpg")) {
                        File image = new File(bon.getPath());
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                        holderHeader.image = bon.getPath();
                        holderHeader.kassenzettel.setImageBitmap(bitmap);
                    }
                }

                holderHeader.ladenName.setText(bon.getShopName());
                holderHeader.datum.setText(bon.getDate());

                if(bon.getAdress() != null && !bon.getAdress().isEmpty()){
                    holderHeader.adresseTitle.setVisibility(View.VISIBLE);
                    holderHeader.adresse.setText(bon.getAdress());
                } else {
                    holderHeader.adresseTitle.setVisibility(View.INVISIBLE);
                    holderHeader.adresse.setText("");
                }

                if(bon.getGuaranteeEnd().contains(".")){
                    holderHeader.garantieTitle.setVisibility(View.VISIBLE);
                    holderHeader.garantie.setText(bon.getGuaranteeEnd());
                } else {
                    holderHeader.garantieTitle.setVisibility(View.INVISIBLE);
                    holderHeader.garantie.setText("");
                }

                holderHeader.gesbetrag.setText(bon.getTotalPrice() + " " + holderHeader.v.getContext().getResources().getString(R.string.waehrung));
                counter++;

            case 1:
                try{
                    ViewHolderBottom holderBottom = (ViewHolderBottom)holder;
                    if(getArticleIndex() < this.artikel.size()){
                        holderBottom.artikel.setText(artikel.get(getArticleIndex()).getName());
                        holderBottom.preis.setText(artikel.get(getArticleIndex()).getPrice() + " " + holderBottom.v.getContext().getResources().getString(R.string.waehrung));
                    }
                } catch(ClassCastException e){
                    e.printStackTrace();
                }
            counter++;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    /**
     * Get the Index of the Article which is shown in the List
     * @return ArticleIndex
     */
    public int getArticleIndex(){

        if (counter > count){
            counter = count - this.recycled + 1;
            this.recycled = 0;
            return counter - 2;
        }
        else {
            this.recycled = 0;
            return counter - 2;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        this.recycled++;
    }
}