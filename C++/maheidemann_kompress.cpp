#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>

using namespace std;

// Funktion zur Berechnung der DCT II 
vector<int> dct(const vector<int>& v) {

    double N =  (double) v.size();
    vector<int> u(N);
    
    for (int k = 0; k < N; k++) {
        double sum = 0.0;
        for (int n = 0; n < N; n++) {
            double n1 = (double) n;
            double k1 = (double) k;
            double arg = (M_PI / N) * (n1 + 0.5) * k1;
            sum += v[n] * cos(arg);
        }
        u[k] = round(sum);
    }
    
    return u;
}

// Funktion zur Berechnung der inversen DCT II ((2/N)*DCT III)
vector<int> inverseDct(const vector<int>& u) {

    double N = (double) u.size();
    vector<int> w(N);
    
    for (int k = 0; k < N; k++) {
        double sum = 0.5*u[0];
        for(int n = 1; n < N; n++) {
            double n1 = (double) n;
            double k1 = (double) k;
            double arg = (M_PI / N)* n1 * (k1 + 0.5);
            sum += u[n] * cos(arg);
        }
        w[k] = round((2.0 / N) * sum);
    }

    return w;
}

// Funktion zum Lesen des Bildes und Durchführen der Bildkompression
void compressImage(const string& inputFileName, const string& outputFileName) {
    ifstream inputFile(inputFileName);
    ofstream outputFile(outputFileName);

    if (!inputFile) {
        cout << "Fehler beim Öffnen der Eingabedatei." << endl;
        return;
    }
    
    if (!outputFile) {
        cout << "Fehler beim Erstellen der Ausgabedatei." << endl;
        return;
    }

    string line;
    int N;
    int M;

    // Header lesen und in die Ausgabedatei schreiben
    for (int i = 0; i < 4; i++) {
        getline(inputFile, line);
        outputFile << line << endl;
        if (i == 2) {
            N = stoi(line);
            M = stoi(line.substr(line.find(' ') + 1));
        }
    }

    // Bildkompression für jede Zeile durchführen
    for (int i = 0; i < M; i++) {

        vector<int> v(N);
        
        // Grauwerte der aktuellen Zeile einlesen
        for (int j = 0; j < N; j++) {
            getline(inputFile, line);
            v[j] = stoi(line);
        }
        
        // DCT II berechnen
        vector<int> u = dct(v);
        
        // Letzte 80% der Einträge von u auf 0 setzen
        int numEntriesToKeep = static_cast<int>(N * 0.2);
        for (int j = numEntriesToKeep; j < N; j++) {
            u[j] = 0;
        }
        
        // Inverse DCT II (DCT III) berechnen
        vector<int> w = inverseDct(u);
        
        // Gerundete Werte in die Ausgabedatei schreiben
        for (int j = 0; j < N; j++) {
            // Jeder Wert zwischen 0 und 255
            if(w[j] < 0) {
                w[j] = 0;
            } else if (w[j] > 255) {
                w[j] = 255;
            }
            outputFile << w[j] << endl;
        }
    }
    
    inputFile.close();
    outputFile.close();
    
    cout << "Bildkompression abgeschlossen. Die Ausgabedatei \"" << outputFileName << "\" wurde erstellt." << endl;
}

int main() {
    
    string inputFileName, outputFileName;
    
    cout << "Name des Eingabebilds: ";
    cin >> inputFileName;
    
    cout << "Name des Ausgabebilds: ";
    cin >> outputFileName;

    compressImage(inputFileName, outputFileName);

    return 0;
}