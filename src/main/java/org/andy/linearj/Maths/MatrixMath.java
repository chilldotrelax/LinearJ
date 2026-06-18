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

import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

public final class MatrixMath {
    private MatrixMath() {
    }

    public static double[][] castStringToDouble(String stringToDouble) {
        try{
            String[] resolvedString = stringToDouble.substring(1,stringToDouble.length()-1).split(";");
            double[][] result = new double[resolvedString.length][resolvedString[0].length() - resolvedString[0].replaceAll("\\s+", "").length() + 1];

            for (String inv : resolvedString){
                String[] cleaned = inv.split(" ");
                for (int i = 0; i < cleaned.length; i++){
                    result[Arrays.asList(resolvedString).indexOf(inv)][i] = Double.parseDouble(cleaned[i]);
                }
            }
            return result;
        }
        catch (ArrayIndexOutOfBoundsException | IllegalAccessError e ) {
            throw new ArrayIndexOutOfBoundsException("Bad state.");
        }

    }

    public static double[][] addMatrix(double[][] a, double[][] b) throws MatrixNotEquivalentException {
        double[][] result = new double[a.length][a[0].length];
    if (a.length != b.length || a[0].length != b[0].length){
        throw new MatrixNotEquivalentException("Error: Not an equivalent matrix.");
        }
    else{
        for (int row = 0; row < a.length; row++){
            for (int col = 0; col < a[row].length; col++){
                result[row][col] = a[row][col] + b[row][col];
                }
            }
        return result;
        }
    }

    public static double[][] subtractMatrix(double[][] a, double[][] b) throws MatrixNotEquivalentException{
        double[][] result = new double[a.length][a.length];

        if (a.length != b.length || a[0].length != b[0].length){
            throw new MatrixNotEquivalentException("Error: Not an equivalent matrix.");
        }
        else{
            for (int row = 0; row < a.length; row++){
                for (int col = 0; col < a[row].length; col++){
                    result[row][col] =  a[row][col] - b[row][col];
                }
            }
            return result;
        }
    }

    public static double[][] multiplyMatrix(double[][] a, double[][] b) throws NonMatchingMatricesException{
        double[][] result = new double[a.length][b[0].length];

        if (a.length != b[0].length){
            throw new NonMatchingMatricesException("Number of rows of A MUST match number of columns of B.");
        }
        else{
            for (int rowA = 0; rowA < a.length; rowA++){
                for (int colB = 0; colB < b[rowA].length; colB++){
                    double sum;
                    for (int rowB = 0; rowB < a.length; rowB++){
                        sum = a[rowA][rowB] * b[rowB][colB];
                        result[rowA][colB] = sum;
                    }
                }
            }
            return result;
        }
    }

    public static double[][] transposeMatrix(double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length];

        if (matrix.length == matrix[0].length){
            for (int row = 0; row < matrix.length; row++){
                for (int col = 0; col < matrix[0].length; col++){
                    result[row][col] = matrix[col][row];
                }
            }
        }
        else if (matrix.length > matrix[0].length){
            for (int col = 0; col < matrix[0].length; col++){
                for (int row = 0; row < matrix.length; row++){
                    result[col][row] = matrix[row][col];
                }
            }
        }
        return result;
    }

    //EJWL Library to compute invert & determinant.
    public static double[][] computeInverse(double[][] matrix) {
        SimpleMatrix a  = new SimpleMatrix(matrix);
        try{
            return a.invert().toArray2();
        }
        catch (SingularMatrixException e){
            throw new SingularMatrixException("Cannot compute the inverse of this matrix -- no general solutions found!");
        }
    }

    public static double computeDeterminant(double[][] matrix) throws ArithmeticException{
       if (matrix.length != matrix[0].length){
           throw new ArithmeticException("Error: Not a square matrix!");
       }
       else{
           SimpleMatrix a = new SimpleMatrix(matrix);
           return a.determinant();
       }
    }


}
