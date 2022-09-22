package gay.nyako.nyakomod.entity.model;

import gay.nyako.nyakomod.entity.PetDragonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 4.4.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class PetDragonModel extends EntityModel<PetDragonEntity> {
	private final ModelPart Head;
	private final ModelPart Horns;
	private final ModelPart cube_r1;
	private final ModelPart Body;
	private final ModelPart cube_r2;
	private final ModelPart Feet;
	private final ModelPart cube_r3;
	private final ModelPart Wings;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	public PetDragonModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		Horns = Head.getChild("Horns");
		cube_r1 = Horns.getChild("cube_r1");
		cube_r2 = Body.getChild("cube_r2");
		Feet = Body.getChild("Feet");
		cube_r3 = Feet.getChild("cube_r3");
		Wings = Body.getChild("Wings");
		leftWing = Wings.getChild("cube_r4");
		rightWing = Wings.getChild("cube_r5");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Head = modelPartData.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -8.0F, -3.0F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 8).cuboid(-3.0F, -6.0F, -5.0F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 12).cuboid(-2.0F, -5.0F, -1.5F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(11, 13).cuboid(-3.0F, -5.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(11, 13).cuboid(1.0F, -5.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, -0.5F));

		ModelPartData Horns = Head.addChild("Horns", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = Horns.addChild("cube_r1", ModelPartBuilder.create().uv(1, 16).cuboid(-2.9F, -10.0F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 18).cuboid(-2.9F, -9.0F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 16).cuboid(0.9F, -10.0F, -3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(7, 18).cuboid(-0.1F, -9.0F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

		ModelPartData Body = modelPartData.addChild("Body", ModelPartBuilder.create().uv(19, 0).cuboid(-2.5F, -10.0F, -2.5F, 4.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(20, 7).cuboid(-2.5F, -7.0F, -1.5F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r2 = Body.addChild("cube_r2", ModelPartBuilder.create().uv(14, 17).cuboid(-0.5F, -0.4853F, -0.2358F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -6.6F, 0.4F, -0.3927F, 0.0F, 0.0F));

		ModelPartData Feet = Body.addChild("Feet", ModelPartBuilder.create(), ModelTransform.pivot(-1.5F, -6.7514F, 0.2844F));

		ModelPartData cube_r3 = Feet.addChild("cube_r3", ModelPartBuilder.create().uv(20, 15).cuboid(1.65F, 0.3937F, -0.5201F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(20, 15).cuboid(-0.65F, 0.3937F, -0.5201F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

		ModelPartData Wings = Body.addChild("Wings", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, -8.5F, 0.5F));

		ModelPartData cube_r4 = Wings.addChild("cube_r4", ModelPartBuilder.create().uv(25, 15).cuboid(-0.2F, -2.5F, -0.45F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, -0.2618F, 0.0F));

		ModelPartData cube_r5 = Wings.addChild("cube_r5", ModelPartBuilder.create().uv(25, 11).cuboid(-2.8F, -2.5F, -0.35F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 0.0F, 0.0F, 0.1745F, 0.2618F, 0.0F));
		return TexturedModelData.of(modelData, 33, 21);
	}
	@Override
	public void setAngles(PetDragonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Wings.setAngles(limbSwing, 0, 0);
		this.Head.setPivot(0, 18.0F, 0);
		// this.Head.pitch = -10;
		this.Head.pitch = headPitch * ((float)Math.PI / 180) / 4;
		this.Head.yaw = netHeadYaw * ((float)Math.PI / 180);
//		this.Head.yaw = 0;
		// this.rightWing.setPivot(0.0f, 0.0f, 0.0f);
		// this.leftWing.setPivot(0.0f, 0.0f, 0.0f);
		this.rightWing.yaw = MathHelper.cos(ageInTicks * 74.48451f * ((float)Math.PI / 180)) * (float)Math.PI * 0.25f;
		this.leftWing.yaw = -this.rightWing.yaw;
		// Head.setAngles(headPitch, netHeadYaw, 0);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		Head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		Body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}