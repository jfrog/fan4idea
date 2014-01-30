package org.fandev.lang.fan.parsing.statements.typeDefinitions.members;

import com.intellij.lang.PsiBuilder;

/**
 * <ul>
 * <li> Mixins contain zero or more uniquely named slots</li>
 * <li> Mixins cannot declare concrete instance fields</li>
 * <li> Mixins cannot declare constructors</li>
 * <li> Mixins cannot declare once methods</li>
 * <li> Mixins can declare abstract methods</li>
 * <li> Mixins can declare concrete instance methods</li>
 * <li> Mixins can declare static methods</li>
 * <li> Mixins can declare abstract fields</li>
 * <li> Mixins can declare static const fields</li>
 * <li> Mixins can declare static constructors: static {}</li>
 * </ul>
 * @author Dror Bereznitsky
 * @date Jan 14, 2009 11:32:29 PM
 */
public class MixinMember {
    public static boolean parse(final PsiBuilder builder, final String className) {
        return true;
    }
}
