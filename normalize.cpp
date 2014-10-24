#include <iostream>
#include "lodepng.h"
#include <cmath>
#include <cstdlib>
#include <cstdio>

using namespace std;


void doIt(int *image, int *temp, int width, int height,double sigma){

	int times[256];
	long long int mean=0;
	long long int variance=0;
	
	for(int i=0;i!=256;i++)times[i]=0;

	// Valuto la distribuzione del colore e la media
	for(int h=0;h<height;h++){
		for(int w=0;w<width;w++){
			times[image[w+h*width]]++;
			mean+=image[w+h*width];
		}
	}
	mean/=(width*height);
	
	// Calcolo la varianza
	for(int i=0;i!=256;i++)variance+=pow(i-mean,2)*times[i];

	variance/=(width*height);

	double stddev=sqrt((double)variance);
	
	cout<<"Mean: "<<mean<<" Variance: "<<variance<<" "<<stddev;

	for(int h=0;h<height;h++){
		for(int w=0;w<width;w++){
			
			//Per una normalizzazione visibile tenete questa parte invece. Ma ripeto non è molto bello usarla 
			//per fare calcoli sull'immagine in seguito...

			//OPTION:2[
			temp[w+h*width]=image[w+h*width]-mean;							//Allinea la media allo 0
			if(temp[w+h*width]<(-(double)sigma*stddev))temp[w+h*width]=-128;					//Satura i pezzi fuori dominio
			else if(temp[w+h*width]>((double)sigma*stddev))temp[w+h*width]=127;						//Satura i pezzi fuori dominio
			else temp[w+h*width]=(double)temp[w+h*width]*((double)sigma*(double)stddev)/(double)256;									//Riscalo il dominio dei colori...
			temp[w+h*width]+=128;
			//]
		}
	}
}

int main(int argc, char*argv[]){
	  unsigned error;
	  unsigned char* image=NULL;
	  unsigned width, height;

	  error = lodepng_decode32_file(&image, &width, &height, argv[1]);
	  if(error) printf("error %u: %s\n", error, lodepng_error_text(error));


	int *temp1=new int[width*height];
	int *temp2=new int[width*height];

	for(int i=0;i!=height*width;i++){
			temp1[i]=image[i*4]+image[i*4+1]+image[i*4+2];
			temp1[i]/=3;
	}
	
	
	doIt((int*)(temp1),(int*)(temp2),width,height,1);
	
	
	for(int i=0;i!=height*width;i++){
			image[i*4]=image[i*4+1]=image[i*4+2]=temp2[i];
			image[i*4+3]=255;
	}
	
	
	error = lodepng_encode32_file(argv[2], image, width, height);
	/*if there's an error, display it*/
	if(error) printf("error %u: %s\n", error, lodepng_error_text(error));
	
	delete temp1;
	delete temp2;
	
	free(image);
}
