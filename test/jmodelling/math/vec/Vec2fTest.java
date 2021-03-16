/*
 * MIT License
 * 
 * Copyright (c) 2021 Antonio GE
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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmodelling.math.vec;

import java.nio.FloatBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ANTONIO
 */
public class Vec2fTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Test of add method, of class Vec2f.
     */
    @Test
    public void testAdd_Vec2f() {
        System.out.println("add");
        Vec2f other = new Vec2f(5.0f, -8.0f);
        Vec2f instance = new Vec2f(1.0f, 3.4f);
        Vec2f expResult = new Vec2f(6.0f, -4.6f);
        Vec2f result = instance.add(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of clone method, of class Vec2f.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Vec2f instance = new Vec2f(5.0f, 1.0f);
        Vec2f expResult = new Vec2f(5.0f, 1.0f);
        Vec2f result = instance.clone();
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class Vec2f.
     */
    @Test
    public void testSet_Vec2f() {
        System.out.println("set");
        Vec2f other = new Vec2f(-3.0f, 5.7f);
        Vec2f instance = new Vec2f(1.0f, 2.0f);
        Vec2f expResult = new Vec2f(-3.0f, 5.7f);
        Vec2f result = instance.set(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of distToSegment method, of class Vec2f.
     */
    @Test
    public void testDistToSegment() {
        System.out.println("distToSegment");
        Vec2f l0 = new Vec2f(1.0f, 1.0f);
        Vec2f l1 = new Vec2f(3.0f, 3.0f);
        Vec2f instance = new Vec2f(3.0f, 1.0f);
        float expResult = 1.41421f;
        float result = instance.distToSegment(l0, l1);
        assertEquals(expResult, result, 0.00001f);
    }

    
    
}
