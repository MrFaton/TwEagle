package com.mr_faton.core.task.util;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 22.09.2015
 */
public interface PostStrategy {
    int QUICK_FAST = 0;
    int FAST = 1;
    int QUICK_MEDIUM = 2;
    int MEDIUM = 3;
    int QUICK_SLOW = 4;
    int SLOW = 5;

    int QUICK_FAST_PERCENT = 15;
    int FAST_PERCENT = 30;
    int QUICK_MEDIUM_PERCENT = 50;
    int MEDIUM_PERCENT = 70;
    int QUICK_SLOW_PERCENT = 85;
    int SLOW_PERCENT = 100;
}

/*

*                                   *
*                                   *
*           *           *           *    <- graph of strategy
*  QF *  F  *  QM *  M  *  QS *  S  *
*************************************
  15%   15%   20%  20%   15%   15%       <-  chance of choice


 */
