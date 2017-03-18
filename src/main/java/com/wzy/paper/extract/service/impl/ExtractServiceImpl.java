package com.wzy.paper.extract.service.impl;

import com.google.common.collect.Lists;
import com.wzy.paper.annotate.Exception.AnnotationException;
import com.wzy.paper.extract.core.ExtractContext;
import com.wzy.paper.extract.core.ExtractRefFeature;
import com.wzy.paper.extract.service.ExtractService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author wzy
 * @Date 2016/11/15 14:23
 */
@Service("ExtractService")
public class ExtractServiceImpl implements ExtractService {

    private  static final  int  MAX_LENGTH=200;

    /** 参考文献长度分成段数 */
    private final int k = 5;

    @Autowired
    private ExtractContext extractContext;

    @Autowired
    private ExtractRefFeature extractRefFeature;

    /* public String extractContext(String filePath) throws IOException, TikaException {
         return extractContext.extractTxt(filePath);
     }*/
//   public List<String> extractRef(String filePath) throws IOException, TikaException {
//
//       List<String> references = Lists.newArrayList();
//
//        references.add(" Erk K, Kowalski A, Pado S, Pinkal M. Towards a resource for lexical semantics: A large german corpus with extensive semantic annotation. In: Hinrichs EW, Roth D, eds. Proc. of the ACL 2003. Sapporo: ACL, 2003. 537−544.\n" );
//
//        return references;
//
//       }
    public List<String> extractRef(String filePath) throws IOException, TikaException {

        String content=extractContext.extractTxt(filePath);

        List<String> references = Lists.newArrayList();

        //匹配参考文献
        Pattern pattern = Pattern.compile("(参.*考.*文.*献)|(参.*考.*文.*献:)|(References:)");
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return Lists.newArrayList();
        }
        //获取最后匹配位置
        int endIndex = matcher.end();
        String refContext = content.substring(endIndex);

        //分割每条参考文献
        references = Lists.newArrayList(refContext.split("\\r|\\n"));

        if(CollectionUtils.isEmpty(references)){
            throw new RuntimeException("未提取到参考文献列表");
        }

        //去除为空的内容

        for(int i=0;i<references.size();i++){
            String reference=references.get(i);

            if(StringUtils.isEmpty(reference)){
                references.remove(i);
            }
        }
        //二次处理
        references= resolve(references);

        return references;
    }

    @Override
    public void extractAndWrite(List<String> refs, String filePath, boolean isLabeled, boolean isAppended) throws IOException, AnnotationException {
        List<List> featureList= extractRefFeature.extractFeatureList(refs,filePath,isLabeled);
    }


    /**
     * 对参考文献进行二次处理
     * @param references
     * @return   TODO
     */
    private List<String> resolve(List<String> references){

        List<String> modifiedReferences = Lists.newArrayList();

        for (int i=0;i<references.size();i++){
            String temp;
            String line1=references.get(i);
            //判断但前是否含有其他字符串
            if(!matcherIndex(line1)) {  //未匹配说明含有其他字符串

                continue; // 跳过当前
            }else {
                temp=line1;
            }

            for (int j=i+1;j<references.size();j++){
                String line2=references.get(j);
                if (!matcherIndex(line2)){ //当前line2行不包含［0-99］
                    temp+=line2;
                }else{

                    temp=removeIndex2Else(temp);
                    if (temp.length()<MAX_LENGTH) {
                        modifiedReferences.add(temp);
                    } //太长（直接抛弃）
                    i=j-1;
                    break;
                }
                if(j==references.size()-1) {//最后一行
                    temp=removeIndex2Else(temp);
                    if (temp.length()<MAX_LENGTH) {
                        modifiedReferences.add(temp);
                    } //太长（直接抛弃）

                }
            }

        }

        return  modifiedReferences;
    }

    /**
     * 匹配参考文献下标［1］
     * @return
     */
    private boolean matcherIndex(String str){

        //匹配[1]|[ 1]|[1 ]|[ 1 ]
        Pattern p = Pattern.compile("(\\s*)(\\[s*)(\\d+)(\\s*])(.*)");
        Matcher matcher=p.matcher(str);
        return  matcher.matches();

    }

    /**
     * 去掉参考文献的下标［1］
     * @param str
     * @return  TODO
     */
    private String removeIndex2Else(String str){

        //1. 先去掉参考文献的下标
        str=str.replaceAll("(\\s*)(\\[s*)(\\d+)(\\s*])(\\s*)", "");

        //2. 去掉期刊页号，之后没有用的内容
        //中文－ 或者英文-  (此处还有问题：存在)
        Pattern pattern=Pattern.compile("(.*)(\\d+)(-)(\\d+)|(.*)(\\d+)(–)(\\d+)");
        Matcher matcher=pattern.matcher(str);

        if (matcher.lookingAt()) { //此处用lookingAt 不用matcher
            str=str.substring(matcher.start(),matcher.end());
        }

        return  str;
    }
}
