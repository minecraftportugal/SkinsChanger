package skinschanger.shared.utils;

import skinschanger.libs.org.json.simple.parser.*;
import skinschanger.libs.com.mojang.api.profiles.*;
import skinschanger.shared.format.*;
import java.util.*;

public class SkinGetUtils
{
    public static SkinProfile getSkinProfile(final String name) throws SkinFetchFailedException {
        try {
            final Profile profile = DataUtils.getProfile(name);
            final SkinProperty prop = DataUtils.getProp(profile.getId());
            return new SkinProfile(prop);
        }
        catch (ParseException e) {
            throw new SkinFetchFailedException(SkinFetchFailedException.Reason.SKIN_RECODE_FAILED);
        }
        catch (SkinFetchFailedException sffe) {
            throw sffe;
        }
        catch (Throwable t) {
            throw new SkinFetchFailedException(t);
        }
    }
    
    public static UUID uuidFromString(final String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
    
    public static class SkinFetchFailedException extends Exception
    {
        private static final long serialVersionUID = -7597517818949217019L;
        
        public SkinFetchFailedException(final Reason reason) {
            super(reason.getExceptionCause());
        }
        
        public SkinFetchFailedException(final Throwable exception) {
            super(String.valueOf(Reason.GENERIC_ERROR.getExceptionCause()) + ": " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
        }
        
        public enum Reason
        {
            NO_PREMIUM_PLAYER("NO_PREMIUM_PLAYER", 0, "Can't find a valid premium player with that name"), 
            NO_SKIN_DATA("NO_SKIN_DATA", 1, "No skin data found for player with that name"), 
            SKIN_RECODE_FAILED("SKIN_RECODE_FAILED", 2, "Can't decode skin data"), 
            GENERIC_ERROR("GENERIC_ERROR", 3, "An error has occured");
            
            private String exceptionCause;
            
            private Reason(final String s, final int n, final String exceptionCause) {
                this.exceptionCause = exceptionCause;
            }
            
            public String getExceptionCause() {
                return this.exceptionCause;
            }
        }
    }
}
