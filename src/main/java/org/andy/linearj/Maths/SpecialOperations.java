package org.andy.linearj.Maths;

/*
Performs more complicated matrix operations
 */

class SpecialOperations {
    public static double[][] getIdentityMatrix(double[][] matrix){
        double[][] identityMatrix = new double[matrix.length][matrix.length];

        try{
            for (int row = 0; row < matrix.length; row++){
                identityMatrix[row][row] = 1;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return identityMatrix;
    }


}
