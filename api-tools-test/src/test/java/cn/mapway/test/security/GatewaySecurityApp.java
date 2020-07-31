package cn.mapway.test.security;

import java.io.FilePermission;

/**
 * 网关安全方案演示程序
 *
 * 展示网站证书内容
 * keytool -printcert --sslserver baidu.com -rfc
 *
 * X.509 是一种通用的证书文件格式可以用ASN.1描述，基于这种格式可以扩展出许多不同类型的证书格式，其编码形式有两种
 * DER(Distinguishing Encoding Rules)通用的二进制格式  PEM(Privacy Enhanced Mail)格式(Base64编码的文件)
 *
 * PKCS#12 个人信息交换语法(PFX) 包含 公钥 私钥  证书链  .p12  .pfx  <Public Key Cryptography>
 *
 * Java领域 将多个证书放在一起存放的格式就是一个Java KeyStore<Repository>,Java KeyStore的类型有5种
 * JKS(SUN)  JCEKS(JCEKS)  PKCS12(WINDOWS) BKS(BouncyCastle)  UBER 每一种可能有不同的软件服务商提供实现
 * JDK中有一个根证书证书集合 放在一起的文件 cacerts  存放路径在 jre/lib/security.Keytool这个工具没有提供方法将私钥导出的能力.
 *
 *
 *  1.利用openssl 制作根证书
 *    > 公私钥              openssl ecparam -genkey -name prime256v1 -noout -out object-root-key.pem
 *    > 生成X.509 证书请求   openssl req  -new -key object-root-key.pem -out object-root-crt.req -subj "/C=CN/ST=Beijing/L=Beijing/O=ziroom.com/OU=iot/CN=object-root"
 *    > 自签名证书           openssl x509 -req -extensions object-root -extfile openssl-ziroom.cnf -in object-root-crt.req -signkey object-root-key.pem -out object-root-crt.pem -days 9999
 *  2. 利用openssl 签署几张用于为其他设备签发证书的CA
 *    > 公私钥              openssl ecparam -genkey -name prime256v1 -noout -out iot-root-key.pem
 *    > 生成X.509 证书请求   openssl req  -new -key iot-root-key.pem -out iot-root-crt.req -subj "/C=CN/ST=Beijing/L=Beijing/O=ziroom.com/OU=iot/CN=iot-root"
 *    >用object-root签发证书 openssl x509 -req -extensions iot-root-ca -extfile openssl-ziroom.cnf -in iot-root-crt.req -CA object-root-crt.pem -CAkey object-root-key.pem -out iot-root-crt.pem -CAcreateserial -days 9999
 *  3. 利用iot证书 签发其他的证书(设备  第三方合作伙伴 服务器 代码签名)
 *     =====代码签名证书=====
 *     openssl ecparam -genkey -name prime256v1 -noout -out codesign-key.pem
 *     openssl req  -new -key codesign-key.pem -out codesign-crt.req -subj "/C=CN/ST=Beijing/L=Beijing/O=ziroom.com/OU=iot/CN=codesign"
 *     openssl x509 -req -extensions iot-codesign -extfile openssl-ziroom.cnf -in codesign-crt.req -CA iot-root-crt.pem -CAkey iot-root-key.pem -out codesign-crt.pem -CAcreateserial -days 9999
 *     =====APP User-CA证书=====
 *     openssl ecparam -genkey -name prime256v1 -noout -out app-user-key.pem
 *     openssl req  -new -key app-user-key.pem -out app-user-crt.req -subj "/C=CN/ST=Beijing/L=Beijing/O=ziroom.com/OU=iot/CN=app-user-ca"
 *     openssl x509 -req -extensions iot-user-ca -extfile openssl-ziroom.cnf -in app-user-crt.req -CA iot-root-crt.pem -CAkey iot-root-key.pem -out app-user-crt.pem -CAcreateserial -days 9999
 *
 *
 *  4.查看证书               openssl x509 -text -noout -in object-root-crt.pem
 *
 */
public class GatewaySecurityApp {
    public static void main(String[] args) {
        GatewaySecurityApp app=new GatewaySecurityApp();
    }
    public void filePermission()
    {

    }
}
