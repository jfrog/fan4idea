package org.fandev.lang.fan.parsing.statements.typeDefinitions.members;

import com.intellij.lang.PsiBuilder;

/**
 * <p>Enums Rules:<ul>
 * <li> Enums are normal classes with all associated characteristics</li>
 * <li> Enums are implied const</li>
 * <li> Enums are implied final</li>
 * <li> Enums have a fixed range of instances</li>
 * <li> Enums can contain declare zero or more uniquely named slots</li>
 * <li> Enums always always inherit from sys::Enum</li>
 * <li> Enums may inherit zero or more mixins</li>
 * <li> Enums must have private constructors</li>
 * </ul></p>
 * <p>Grammar Definition:<ul>
 * <li>&lt;enumValDefs&gt;    :=  &lt;enumValDef&gt; ("," &lt;enumValDef&gt;)* &lt;eos&gt;</li>
 * <li>&lt;enumValDef&gt;     :=  &lt;id&gt; ["(" &lt;args&gt; ")"]</li>
 * </ul></p>
 *
 * @author Dror Bereznitsky
 * @author Fred Simon
 * @date Jan 14, 2009 11:58:24 PM
 */
public class EnumMember {
    public static boolean parse(final PsiBuilder builder, final String className) {
        return true;
    }
}