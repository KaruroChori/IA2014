//Ho solo iniziato ad implementarlo, non ho avuto più tempo, lo riprendiamo venerdì. 
//Devo ancora inserire tutto in una classe, sarà da vedere come strutturarla.

int parametrize_feature_index(int type, int i, int j, int w, int h){
    //Eval counter from the previous parameters...
    return something;
}

int[] parametrize_feature_index_inv(int counter){
    //Eval all indexes from the counter...
    return something;
}

//TODO: use integral image to evaluate Sn;

void compute24x24_feature_vector(IntegralImage img, int[] features){
    
    int counter=0;

	//Feature A
    for(int i=1;i<=24;i++)
    for(int j=1;j<=24;j++){
        for(int w=1;j+2*w-1<=24)
        for(int h=1;i+h-1<=24){
            int S1;
            int S2;

            features[counter]=S1-S2;
            counter++;    
        }
    }	

	//Feature B
    for(int i=1;i<=24;i++)
    for(int j=1;j<=24;j++){
        for(int w=1;j+3*w-1<=24)
        for(int h=1;i+h-1<=24){
            int S1;
            int S2;
            int S3;

            features[counter]=S1-S2+S3;
            counter++;    
        }
    }

	//Feature C
    for(int i=1;i<=24;i++)
    for(int j=1;j<=24;j++){
        for(int w=1;j+w-1<=24)
        for(int h=1;i+2*h-1<=24){
            int S1;
            int S2;

            features[counter]=S1-S2;
            counter++;    
        }
    }

	//Feature D
    for(int i=1;i<=24;i++)
    for(int j=1;j<=24;j++){
        for(int w=1;j+w-1<=24)
        for(int h=1;i+3*h-1<=24){
            int S1;
            int S2;
            int S3;

            features[counter]=S1-S2+S3;
            counter++;    
        }
    }


	//Feature E
    for(int i=1;i<=24;i++)
    for(int j=1;j<=24;j++){
        for(int w=1;j+2*w-1<=24)
        for(int h=1;i+2*h-1<=24){
            int S1;
            int S2;
            int S3;
            int S4;

            features[counter]=S1-S2-S3+S4;
            counter++;    
        }
    }
}

// Working on it...

int feature_scaling(IntegralImage img, int type, int i, int j, int w, int h){
    switch(type){
        case 0:     //Feature A
            i/=24;  //Scaling
            j/=24;
            h/=24;
            w=
            break;

        case 1:     //Feature B
            break;

        case 2:     //Feature C
            break;

        case 3:     //Feature D
            break;

        case 4:     //Feature E
            break;
         


    }
}
