/**
 Copyright (c) 2015, Bartłomiej Konieczny
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the Bartłomiej Konieczny.
 4. Neither the name of the Bartłomiej Konieczny nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Bartłomiej Konieczny ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Bartłomiej Konieczny BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.ToString;

import static com.badlogic.gdx.math.MathUtils.sin;

/**
 * <p>
 *     Basic square class designed for LibGdx rendering.
 * </p>
 * Created by Bartłomiej Konieczny on 2015-10-17.
 */
@ToString
public class Square {

    private int x,y;
    private int width, height;

    public Square(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draws a square.
     * @param shapeRenderer renderer to draw to
     */
    public void draw(final ShapeRenderer shapeRenderer){

        shapeRenderer.rect(x , y, width, height);

    }

    /**
     * Moves all squares on the screen.
     * @param deltaTime time since last frame update
     */
    public void applyMovement(float deltaTime) {
// TODO: 2015-11-08 Movement of the environment

    }
}
