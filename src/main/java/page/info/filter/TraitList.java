package page.info.filter;

import common.CommonStatic;
import common.util.unit.Trait;
import common.pack.Source;
import common.pack.UserProfile;
import common.pack.Context;
import main.MainBCU;
import utilpc.Interpret;
import utilpc.Theme;
import utilpc.UtilPC;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class TraitList extends JList<Trait> {

    private static final long serialVersionUID = 1L;
    public final Vector<Trait> list = new Vector<>();
    public final boolean id;

    //editing is used to set whether the page using this is Trait Edit Page or not. May be used to add BC Traits to list at some point
    public TraitList(boolean editing) {
        id = editing;

        if (MainBCU.nimbus)
            setSelectionBackground(MainBCU.light ? Theme.LIGHT.NIMBUS_SELECT_BG : Theme.DARK.NIMBUS_SELECT_BG);

        setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object o, int ind, boolean s, boolean f) {
                JLabel jl = (JLabel) super.getListCellRendererComponent(l, o, ind, s, f);
                Trait trait = (Trait)o;
                if (trait.BCTrait) {
                    jl.setText(Interpret.TRAIT[trait.id.id]);
                    jl.setIcon(UtilPC.createIcon(3, trait.id.id));
                } else {
                    if (!editing)
                        jl.setText(trait.name);
                    if (trait.icon != null){
                        BufferedImage icon = (BufferedImage)trait.icon.getImg().bimg();
                        if(icon.getWidth() != icon.getHeight()) {
                            File file =((Source.Workspace) UserProfile.getUserPack(trait.id.pack).source).getTraitIconFile(trait.id);
                            if (file.delete()) {
                                trait.icon = null;
                            }
                            icon = (BufferedImage) CommonStatic.getBCAssets().dummyTrait.getImg().bimg();
                        } else if (icon.getWidth() != 41 || icon.getHeight() != 41) {
                            icon = resizeImage(icon, 41, 41);
                            try {
                                File file = ((Source.Workspace) UserProfile.getUserPack(trait.id.pack).source).getTraitIconFile(trait.id);
                                Context.check(file);
                                ImageIO.write(icon, "PNG", file);
                                trait.icon = MainBCU.builder.toVImg(icon);
                            } catch (IOException e) {
                                icon = (BufferedImage) CommonStatic.getBCAssets().dummyTrait.getImg().bimg();
                            }
                        }
                        jl.setIcon(new ImageIcon(icon));
                    }
                    else
                        jl.setIcon(new ImageIcon((BufferedImage) CommonStatic.getBCAssets().dummyTrait.getImg().bimg()));
                }
                return jl;
            }
        });
    }

    public void setListData() {
        setListData(list);
    }

    public static BufferedImage resizeImage(BufferedImage img,int w, int h){
        Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}