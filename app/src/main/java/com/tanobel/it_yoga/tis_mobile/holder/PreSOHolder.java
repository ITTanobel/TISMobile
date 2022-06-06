package com.tanobel.it_yoga.tis_mobile.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
import com.tanobel.it_yoga.tis_mobile.R;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class PreSOHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {


    public PreSOHolder(Context context) {
        super(context);
    }
    private PrintView arrowView;

    @Override
    public View createNodeView(final TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_preso_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        TextView tvCustkrm = view.findViewById(R.id.custkirim);
        TextView tvAlamat = view.findViewById(R.id.alamat);
        TextView tvTglkrm = view.findViewById(R.id.tglkirim);
        TextView tvStatus = view.findViewById(R.id.status);

        tvValue.setText(value.text);
        tvCustkrm.setText(value.custkrm);
        tvAlamat.setText(value.alamat);
        tvTglkrm.setText(value.tgl);
        tvStatus.setText(value.status);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tView.toggleNode(node);
            }
        });

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }
    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
