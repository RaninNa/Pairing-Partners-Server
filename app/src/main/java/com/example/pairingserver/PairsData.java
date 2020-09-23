package com.example.pairingserver;

public class PairsData {
    private Partner[] partners;
    private int count;
    public PairsData()
    {
        count=0;
    }

    public void AddPartner(Partner partner)
    {
        if(count<partners.length)
            partners[count] = partner;
        else
            return;
        count++;

    }
    public Partner[] getPartners() {
        return this.partners;
    }

    public void setPartners(final Partner[] partners) {
        this.partners = partners;
    }
}
