package com.example.pairingserver;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class Helpers {
    public static int GetScore(Student student1, Student student2) {

        float ScoreSt1 = GetScoreSide(student1, student2);
        float ScoreSt2 = GetScoreSide(student2, student1);
        int Score = (int) ((ScoreSt1 + ScoreSt2) / 2);
        return Score;
    }

    public static float GetScoreSide(Student student1, Student student2) {
        float Portions = 0;
        float ScoreSt = 0;
        float[] Criterions = new float[6];//Location-Grade-Workplan-meeting-prefgen-hours
        if (student1.isLocation_flag())
            Criterions[0] = 1;
        else
            Criterions[0] = 0.5f;

        if (student1.isGPA_flag())
            Criterions[1] = 1;
        else
            Criterions[1] = 0.5f;

        if (student1.getPreferred_work_plan().equals("לא משנה"))
            Criterions[2] = 0.5f;
        else
            Criterions[2] = 1;

        if (student1.getPreferred_meetings().equals("לא משנה"))
            Criterions[3] = 0.5f;
        else
            Criterions[3] = 1;


        if (student1.getPreferred_gender().equals("לא משנה"))
            Criterions[4] = 0.5f;
        else
            Criterions[4] = 1;

        if (student1.getPreferred_hours().equals("לא משנה"))
            Criterions[5] = 0.5f;
        else
            Criterions[5] = 1;

        for (int i = 0; i < 6; i++)
            Portions += Criterions[i];
        //Calculate ScoreSt1
        //0
        if (student1.isLocation_flag())
            ScoreSt += (GetLocationScore(student1.getLocation(), student2.getLocation()) / 100.0f) * (100 / Portions * Criterions[0]);
        else
            ScoreSt += (100 / Portions) * Criterions[0];
        //1
        if (student1.isGPA_flag())
            ScoreSt += (student2.getGPA() / 100.0f) * (100 / Portions * Criterions[1]);
        else
            ScoreSt += (100 / Portions) * Criterions[1];
        //2
        if (student1.getPreferred_work_plan().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[2];
        else if (student1.getPreferred_work_plan().equals(student2.getPreferred_work_plan()))
            ScoreSt += (100 / Portions) * Criterions[2];
        else
            ScoreSt += 0;
        //3
        if (student1.getPreferred_meetings().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[3];
        else if (student1.getPreferred_meetings().equals(student2.getPreferred_meetings()))
            ScoreSt += (100 / Portions) * Criterions[3];
        else
            ScoreSt += 0;
        //4
        if (student1.getPreferred_gender().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[4];
        else if (student1.getPreferred_gender().equals(student2.getGender()))
            ScoreSt += (100 / Portions) * Criterions[4];
        else
            ScoreSt += 0;

        //5
        if (student1.getPreferred_hours().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[5];
        else
            ScoreSt += (GetDayHourWorkingScore(student1.getPreferred_hours(), student2.getPreferred_hours()) / Portions) * Criterions[5];

        return ScoreSt;
    }

    public static double GetDayHourWorkingScore(String DH1, String DH2) {
        try {
            String[] St1 = DH1.split("-");
            String[] St2 = DH2.split("-");
            String Student1Days = St1[0];
            String Student2Days = St2[0];
            String Student1Hours = St1[1];
            String Student2Hours = St2[1];
            String[] S1Days = Student1Days.split("@");
            String[] S2Days = Student2Days.split("@");
            String[] S1Hours = Student1Hours.split("@");
            String[] S2Hours = Student2Hours.split("@");
            int st1days = S1Days.length;
            int st2days = S2Days.length;
            int st1hours = S1Hours.length;
            int st2hours = S2Hours.length;
            double countCommonDays = 0;
            for (int i = 0; i < st1days; i++) {
                if (Student2Days.contains(S1Days[i])) {
                    countCommonDays++;
                }
            }
            double totalDays = st1days + st2days - countCommonDays; // תורת הקבוצות :)
            double countCommonHours = 0;
            for (int i = 0; i < st1hours; i++) {
                if (Student2Hours.contains(S1Hours[i])) {
                    countCommonHours++;
                }
            }
            double totalHours = st1hours + st2hours - countCommonHours;
            double scoreDays = 0;
            if (countCommonDays > 3)
                scoreDays = 1;
            else if (countCommonDays >= 2)
                scoreDays = 0.8f;
            else
                scoreDays = countCommonDays / totalDays;
            double scoreHours = 0;
            if (countCommonHours > 2)
                scoreHours = 1;
            else
                scoreHours = countCommonHours / totalHours;
            double TotalScore = scoreDays * scoreHours;

            return TotalScore*100;

        } catch (Exception ex) {
            return 10;
        }

    }

    public static double GetLocationScore(String Loc1, String Loc2) {

        if (Loc1.equals("") || Loc2.equals("")) {
            return 0;
        }
        try {

            String[] L1 = Loc1.split("@");
            String[] L2 = Loc2.split("@");
            LatLng latLng1 = new LatLng(Double.parseDouble(L1[0]), Double.parseDouble(L1[0]));
            LatLng latLng2 = new LatLng(Double.parseDouble(L2[0]), Double.parseDouble(L2[1]));
            double Dis = CalculationByDistance(latLng1, latLng2);
            double Res = (((60 - Dis) / 60) * 100);
            if (Res < 10)
                return 10;
            return Res;

        } catch (Exception ex) {
            return 10;
        }
        /*
        int i1 = 0, i2 = 0;

        String[] Locs = getResources().getStringArray(R.array.location_array);
        for (int i = 1; i < Locs.length; i++) {
            if (Locs[i].equals(Loc1))
                i1 = i;
            if (Locs[i].equals(Loc2))
                i2 = i;
        }

        float[][] LocationScores = new float[Locs.length][Locs.length];
        LocationScores[1][1] = 100;
        LocationScores[2][2] = 100;
        LocationScores[3][3] = 100;
        LocationScores[4][4] = 100;
        LocationScores[1][2] = LocationScores[2][1] = 26;
        LocationScores[1][3] = LocationScores[3][1] = 67;
        LocationScores[1][4] = LocationScores[4][1] = 70;

        LocationScores[2][3] = LocationScores[3][2] = 0;
        LocationScores[2][4] = LocationScores[4][2] = 0;

        LocationScores[4][3] = LocationScores[3][4] = 83;
                return LocationScores[i1][i2];
        */


    }

    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return valueResult;//Radius * c;
        /*
        LatLng latLng;
        Double l1=latlng.latitude;
        Double l2=latlng.longitude;
        String coordl1 = l1.toString();
        String coordl2 = l2.toString();
        l1 = Double.parseDouble(coordl1);
        l2 = Double.parseDouble(coordl2);


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(l1, l2))
                .title(title)
                .snippet(info));
                */

    }


    public static void RemoveRowColScores(int n)
    {
        int sourcearr[][] = Globals.pairs_scores;
        int destinationarr[][] = new int[Globals.pairs_scores.length-1][Globals.pairs_scores.length-1];

        int REMOVE_ROW = n;
        int REMOVE_COLUMN = n;
        int p = 0;
        for( int i = 0; i < Globals.pairs_scores.length; ++i)
        {
            if ( i == REMOVE_ROW)
                continue;


            int q = 0;
            for( int j = 0; j < Globals.pairs_scores.length; ++j)
            {
                if ( j == REMOVE_COLUMN)
                    continue;

                destinationarr[p][q] = sourcearr[i][j];
                ++q;
            }
            ++p;
        }
        Globals.pairs_scores = destinationarr;
    }

    public static void RemoveRowColStudents(int n) {
        Student sourcearr[] = Globals.students;
        Student destinationarr[] = new Student[Globals.students.length - 1];

        int REMOVE_ROW = n;
        int p = 0;
        for (int i = 0; i < Globals.students.length; ++i) {
            if (i == REMOVE_ROW)
                continue;
            destinationarr[p] = sourcearr[i];
            p++;
        }
        Globals.students = destinationarr;
        for (int i = 0; i < Globals.students.length; i++) {
            Globals.students[i].setNo(i);
        }
    }

    public static int[][] CheckScores(int[][] scores)
    {

        return  scores;
    }
}
