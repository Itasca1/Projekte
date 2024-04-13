#include <iostream>
#include <vector>
#include <cmath>
#include <unordered_set>

using namespace std;

// Choleskyzerlegung, gibt Matrix L zurück
vector<vector<double>> cholesky(const vector<vector<double>>& A) {
    vector<vector<double>> L(A.size(), vector<double>(A.size(), 0.0));

    for (int j = 0; j < A.size(); j++) {
        double summe1 = 0.0;
        for (int k = 0; k < j; k++) {
            summe1 += L[j][k] * L[j][k];
        }
        L[j][j] = sqrt(A[j][j] - summe1);

        for (int i = j + 1; i < A.size(); i++) {
            double summe2 = 0.0;
            for (int k = 0; k < j; k++) {
                summe2 += L[i][k] * L[j][k];
            }
            L[i][j] = (A[i][j] - summe2) / L[j][j];
        }
    }

    return L;
}

// Vorwärtssubstitution: L*y = b, gibt Vektor y zurück
vector<double> forwardSub(const vector<vector<double>>& L, const vector<double>& b) {
    vector<double> y(L.size(), 0.0);

    for (int i = 0; i < L.size(); i++) {
        double sum = 0.0;
        for (int j = 0; j < i; j++) {
            sum += L[i][j] * y[j];
        }
        y[i] = (b[i] - sum) / L[i][i];
    }

    return y;
}

// Rückwärtssubstitution: L^T*x = y, gibt Vektor x zurück
vector<double> backwardSub(const vector<vector<double>>& Lt, const vector<double>& y) {
    vector<double> x(Lt.size(), 0.0);

    for (int i = Lt.size() - 1; i >= 0; i--) {
        double sum = 0.0;
        for (int j = i + 1; j < Lt.size(); j++) {
            sum += Lt[j][i] * x[j];
        }
        x[i] = (y[i] - sum) / Lt[i][i];
    }

    return x;
}

int main() {

    // Set für die Eingabe der x-Werte, stored nur unique values
    std::unordered_set<int> numbers; 

    //Definiere m und den Vektor y
    int m;
    vector<double> y;

    //Einlesen von m
    cout << "Ausgleichparabel. Bitte die Anzahl m der Datenpunkte eingeben: ";
    cin >> m;

    // Matrix mit m Zeilen und 3 Spalten
    vector<vector<double>> matrixA(m, vector<double>(3)); 

    // Einlesen der Werte
    for (int i = 0; i < m; i++) {
        double xValue, yValue;
        cout << "(x" << i + 1 << ", y" << i + 1 << ") eingeben: ";
        char comma;
        cin >> xValue >> comma >> yValue;
        // Befüllen der Matrix und des Vektors y, sowie hinzufügen der unique values zu numbers
        numbers.insert(xValue);
        matrixA[i][0] = 1.0;       
        matrixA[i][1] = xValue;      
        matrixA[i][2] = xValue * xValue;     
        y.push_back(yValue);
    }

    // Check ob es mindestens 3 verschiedene x Werte gibt
    if (numbers.size() < 3) {
        std::cout << "Keine Loesung, denn A hat keinen vollen Rang. \n";
        return 0;
    } 
    
    //Erstelle Matrizen A^T*A und A^T*y
    vector<vector<double>> AtA(matrixA[0].size(), vector<double>(matrixA[0].size(), 0.0));
    vector<double> Aty(matrixA[0].size(), 0.0);

    // Berechnung von A^T*A und A^T*y
    for (int i = 0; i < matrixA[0].size(); i++) {
        for (int j = 0; j < matrixA[0].size(); j++) {
            for (int k = 0; k < m; k++) {
                AtA[i][j] += matrixA[k][i] * matrixA[k][j];
            }
        }
        for (int k = 0; k < m; k++) {
            Aty[i] += matrixA[k][i] * y[k];
        }
    }

    // Cholesky-Zerlegung
    vector<vector<double>> L = cholesky(AtA);

    // Vorwärtssubstitution: L*y = A^T*y
    vector<double> ySolution = forwardSub(L, Aty);

    // Rückwärtssubstitution: L^T*x = y
    vector<double> xSolution = backwardSub(L, ySolution);

    // Ausgabe des Approximationspolynoms
    cout << "Das Approximationspolynom ist " << xSolution[2] << "x^2 + " << xSolution[1] << "x + " << xSolution[0] << ".\n" << endl;

    return 0;
}