package skinschanger.libs.org.json.simple.parser;

import java.util.*;

public interface ContainerFactory
{
    Map<?, ?> createObjectContainer();
    
    List<?> creatArrayContainer();
}
