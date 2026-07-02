/*
 * MIT License
 *
 * Copyright © 2026 Andy Huang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.andy.linearj.Maths;

import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;
import org.ejml.data.SingularMatrixException;

public class LUDecomposition {
    private final int[] indexOfPermutation;
    private final double [][] decomposedLU;
    private final int numRows;

    public LUDecomposition(double[][] admittanceMatrix) throws IllegalMatrixException {
        this.decomposedLU = new double[admittanceMatrix.length][];//Output

        for (int i = 0; i < admittanceMatrix.length; i++){
            this.decomposedLU[i] = admittanceMatrix[i].clone();
        }
        this.numRows = admittanceMatrix.length;
        this.indexOfPermutation = new int[this.numRows]; //Permutation Matrix

        final double SMALL_CONST = 1.0e-40;
        double big;
        double temp;
        int imax = 0;
        double[] impliedScaling = new double[this.numRows]; //Implicit Scaling
        double oddOrEven = 1.0;

        for (int i = 0; i < this.numRows; i++){
            big = 0.0;
            for (int j = 0; j < this.numRows; j++){
                temp = Math.abs(this.decomposedLU[i][j]);
                if (temp > big){
                    big = temp;
                }
            }
            if (big == 0.0){
                throw new SingularMatrixException("Singular matrix detected");
            }
            impliedScaling[i] = 1.0/big;
        }

        for (int k = 0; k < this.numRows; k++){
            big = 0.0;
            for (int i = k; i < this.numRows; i++){
                temp = impliedScaling[i] * Math.abs(this.decomposedLU[i][k]);
                if (temp > big){
                    big = temp;
                    imax = i;
                }
            }
            //Row swaps needed?
            if (k != imax){
                for (int j = 0; j < this.numRows; j++){
                    temp = this.decomposedLU[imax][j];
                    this.decomposedLU[imax][j] = this.decomposedLU[k][j];
                    this.decomposedLU[k][j] = temp;
                }
                oddOrEven = -oddOrEven;
                impliedScaling[imax] = impliedScaling[k]; //Interchange scale factor.
        }
            this.indexOfPermutation[k] = imax; //Store the row.
            if (this.decomposedLU[k][k] == 0.0){
                this.decomposedLU[k][k] = SMALL_CONST;
            }

            for (int i = k + 1; i < this.numRows; i++){
                temp = this.decomposedLU[i][k] / this.decomposedLU[k][k];
                this.decomposedLU[i][k] = temp;
                for (int j = k + 1; j < this.numRows; j++){
                    this.decomposedLU[i][j] -= temp * this.decomposedLU[k][j];
                }
            }
        }
    }

    //For A * x = b with n linear equations, return sol vector x.
    public double[] solve(double[] RHS,double[] X){
        int ip;
        int ii = 0;
        double sum;

        if (RHS.length != this.numRows || X.length != this.numRows){
            throw new IllegalMatrixException("Bad sizes");
        }

        for (int i = 0; i < this.numRows; i++){
            X[i] = RHS[i];
        }

        for (int i = 0; i < this.numRows; i++){
            ip = this.indexOfPermutation[i];
            sum = X[ip];
            X[ip] = X[i];

            if (ii != 0){
                for (int j = ii - 1; j < i; j++){
                    sum -= this.decomposedLU[i][j] * X[j];
                }
            }
            else if (sum != 0.0){
                ii = i + 1;
            }
            X[i] = sum;
        }

        //Perform a back substitution
        for (int i = this.numRows - 1; i >= 0; i--){
            sum = X[i];
            for (int j = i + 1; j < this.numRows; j++){
                sum -= this.decomposedLU[i][j] * X[j];
            }
            X[i] = sum / this.decomposedLU[i][i];
        }
        return X;
    }
}
