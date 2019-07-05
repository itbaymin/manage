package com.byc.common.util;

/**
 * Created by baiyc
 * 2019/7/5/005 17:14
 * Description：
 */
public class FileTypeUtil {

    private static String[] audio = {"mp3"};
    private static String[] video = {"mp4","rmvb","flv","avi"};
    private static String[] code = {"js","css","java","scss","php","c","cpp"};
    private static String[] picture = {"png","jpg","gif"};
    private static String[] word = {"doc","docx"};
    private static String[] excel = {"xls","xlsx"};
    private static String[] ppt = {"ppt"};
    private static String[] pdf = {"pdf"};
    private static String[] zip = {"zip","rar"};
    private static String[] text = {"txt"};

    public static Type getFileType(String suffix){
        if(suffix==null||suffix.equals(""))
            return Type.DIR;
        else if(contains(audio,suffix))
            return Type.AUDIO;
        else if(contains(video,suffix))
            return Type.VIDEO;
        else if(contains(code,suffix))
            return Type.CODE;
        else if(contains(picture,suffix))
            return Type.PICTURE;
        else if(contains(word,suffix))
            return Type.WORD;
        else if(contains(excel,suffix))
            return Type.EXCEL;
        else if(contains(ppt,suffix))
            return Type.PPT;
        else if(contains(pdf,suffix))
            return Type.PDF;
        else if(contains(zip,suffix))
            return Type.ZIP;
        else if(contains(text,suffix))
            return Type.TEXT;
        else
            return Type.OTHER;
    }

    private static boolean contains(String[] suffixs,String suffix){
        for (String suf:suffixs){
            if(suf.equalsIgnoreCase(suffix)){
                return true;
            }
        }
        return false;
    }

    public enum Type{
        AUDIO("fa-file-audio-o"),
        VIDEO("fa-file-movie-o"),
        CODE("fa-file-code-o"),
        PICTURE("fa-file-image-o"),
        WORD("fa-file-word-o"),
        EXCEL("fa-file-excel-o"),
        PPT("fa-file-powerpoint-o"),
        PDF("fa-file-pdf-o"),
        ZIP("fa-file-zip-o "),
        OTHER("fa-file"),
        DIR("fa-folder"),
        TEXT("fa-file-text-o");

        private String icon;

        Type(String icon){
            this.icon = icon;
        }

        String getIcon(){
            return this.icon;
        }
    }
}
