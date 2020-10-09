package com.example.arapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String MODEL_URL="https://github.com/EasylearnIndia/EasyLearn/blob/Augmented-Reality/EasyLearn/Assets/Models/tiger/tiger.glb?raw=true";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        setUpModel();
        setUpPlane();

    }

    private void setUpModel() {

            ModelRenderable.builder()
                    .setSource(this,
                            RenderableSource.builder().setSource(
                                    this,
                                    Uri.parse(MODEL_URL),
                                    RenderableSource.SourceType.GLB)
                                    .setScale(0.75f)
                                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                    .build())
                    .setRegistryId(MODEL_URL)
                    .build()
                    .thenAccept(renderable -> modelRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast.makeText(MainActivity.this,"Can't load the model", Toast.LENGTH_SHORT).show();
                        return null;
                    });

    }
    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            creatModel(anchorNode);
        }));
    }

    private  void  creatModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }
}