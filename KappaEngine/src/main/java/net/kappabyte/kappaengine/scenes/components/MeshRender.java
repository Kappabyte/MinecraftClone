package net.kappabyte.kappaengine.scenes.components;

import net.kappabyte.kappaengine.graphics.Mesh;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.materials.Material;

public class MeshRender extends Renderable {

    Mesh mesh;

    public MeshRender(Mesh mesh, Material material, boolean staticGeometry) {
        super(material, staticGeometry);

        this.mesh = mesh;
    }

    @Override
    protected RenderData supplyRenderData() {
        return new RenderData(getTransform(), mesh, material, getGameObject());
    }

}
