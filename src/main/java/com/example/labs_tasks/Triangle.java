package com.example.labs_tasks;

import javafx.scene.paint.Color;

class Triangle extends Shape{
    int x, y;
    double alf, bet, gam;
    Triangle(int x, int y, double alf, double bet, double gam, Color color){
        super(color);
        this.x = x;
        this.y = y;
        this.alf = alf;
        this.bet = bet;
        this.gam = gam;
    }
}
