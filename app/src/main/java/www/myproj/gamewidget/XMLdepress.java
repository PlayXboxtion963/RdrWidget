package www.myproj.gamewidget;

import android.os.Build;
import android.util.Xml;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLdepress {
    public String location;
    public String city;
    public String temp;
    public boolean statue;
   public void saxdrpess(String in) throws ParserConfigurationException, SAXException, IOException{
       SAXParserFactory factory = SAXParserFactory.newInstance();
       // 2.从解析工厂获取解析器
       SAXParser parse = factory.newSAXParser();
       // 3.得到解读器
       XMLReader reader=parse.getXMLReader();
       // 4.设置内容处理器
       PHandler mhander=new PHandler();
       reader.setContentHandler(mhander);
       // 5.读取xml的文档内容
       reader.parse(new InputSource(new StringReader(in)));
        System.out.println(in);

   }
    class PHandler extends DefaultHandler {
       private int flag=0;

        /**
         * @author lastwhisper
         * @desc 文档解析开始时调用，该方法只会调用一次
         * @param
         * @return void
         */
        @Override
        public void startDocument() throws SAXException {

        }

        /**
         * @author lastwhisper
         * @desc 每当遇到起始标签时调用
         * @param uri xml文档的命名空间
         * @param localName 标签的名字
         * @param qName 带命名空间的标签的名字
         * @param attributes 标签的属性集
         * @return void
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if(qName.equals("province")) {
                flag = 2;
            }else if(qName.equals("city")){

                flag=3;
            }else if(qName.equals("temperature")){
                flag=4;
            }else if(qName.equals("status")){
                flag=10;
            }
            else{
                flag=0;
            }
        }

        /**
         * @author lastwhisper
         * @desc 解析标签内的内容的时候调用
         * @param ch 当前读取到的TextNode(文本节点)的字节数组
         * @param start 字节开始的位置，为0则读取全部
         * @param length 当前TextNode的长度
         * @return void
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String contents = new String(ch, start, length).trim();
            if (contents.length() > 0) {

                if(flag==2){
                    city=contents;

                }
                if(flag==3){
                    location=contents;

                    String chinese = location;
                    char[] chars = chinese.toCharArray();

                    StringBuffer buffer = new StringBuffer(); //储存结果

                    //转换函数用到的一些配置
                    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                    format.setCaseType(HanyuPinyinCaseType.UPPERCASE);  //转小写
                    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); //不带音标

                    for(int i = 0; i < chars.length; ++i){
                        if(chars[i] > 128){
                            try{
                                buffer.append(PinyinHelper.toHanyuPinyinStringArray(chars[i],format)[0]);  //转换出的结果包含了多音字，这里简单粗暴的取了第一个拼音。
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{ //非汉字
                            buffer.append(chars[i]);
                        }
                    }

                    location=buffer.toString(); //最终的结果"ceshidezifuchuan"
                }
                if(flag==4){
                    temp=contents;

                }
                if(flag==10){
                    if(contents.equals("1")){
                        statue=true;
                    }else{
                        statue=false;
                    }
                }
            }
        }
        /**
         * @author lastwhisper
         * @desc 每当遇到结束标签时调用
         * @param uri xml文档的命名空间
         * @param localName 标签的名字
         * @param qName 带命名空间的标签的名字
         * @return void
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

        }
        /**
         * @author lastwhisper
         * @desc 文档解析结束后调用，该方法只会调用一次
         * @param
         * @return void
         */
        @Override
        public void endDocument() throws SAXException {
            System.out.println("----解析文档结束----");
        }
    }
}
