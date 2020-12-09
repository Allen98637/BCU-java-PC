package page.battle;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.battle.SBCtrl;
import common.system.P;
import common.system.fake.FakeImage;
import common.util.unit.Form;
import page.battle.BattleBox.BBPainter;
import page.battle.BattleBox.OuterBox;
import utilpc.PP;

import java.awt.*;
import java.awt.event.MouseEvent;

public class BBCtrl extends BBPainter {

	private final SBCtrl sbc;

	public BBCtrl(OuterBox bip, SBCtrl bas, BattleBox bb) {
		super(bip, bas, bb);
		sbc = bas;
	}

	@Override
	public synchronized void click(Point p, int button) {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		int w = box.getWidth();
		int h = box.getHeight();
		double hr = unir;
		double term = hr * aux.slot[0].getImg().getWidth() * 0.2;
		for (int i = 0; i < 5; i++) {
			Form f = sbc.sb.b.lu.fs[sbc.sb.frontLineup][i % 5];
			FakeImage img = f == null ? aux.slot[0].getImg() : f.anim.getUni().getImg();
			int iw = (int) (hr * img.getWidth());
			int ih = (int) (hr * img.getHeight());
			int x = (w - iw * 5) / 2 + iw * (i % 5) + (int) (term * ((i % 5) -2) + (sbc.sb.frontLineup == 0 ? 0 : term/2));
			int y = h - (int) (ih * 1.1);
			if (!new PP(p).out(new P(x, y), new P(x + iw, y + ih), 0))
				sbc.action.add(i+sbc.sb.frontLineup*5);
			if (button != MouseEvent.BUTTON1)
				sbc.action.add(10);
		}
		hr = corr;
		FakeImage left = aux.battle[0][0].getImg();
		FakeImage right = aux.battle[1][0].getImg();
		int ih = (int) (hr * left.getHeight());
		int iw = (int) (hr * left.getWidth());
		if (!new PP(p).out(new P(0, h - ih), new P(iw, h), 0))
			sbc.action.add(-1);
		iw = (int) (hr * right.getWidth());
		ih = (int) (hr * right.getHeight());
		if (!new PP(p).out(new P(w - iw, h - ih), new P(w, h), 0))
			sbc.action.add(-2);

		if ((sbc.sb.conf[0] & 2) > 0) {
			FakeImage bimg = aux.battle[2][1].getImg();
			int cw = bimg.getWidth();
			int ch = bimg.getHeight();
			int mh = aux.num[0][0].getImg().getHeight();
			if (!new PP(p).out(new P(w - cw, mh), new P(w, mh + ch), 0))
				sbc.action.add(-3);
		}
		reset();
	}

}
