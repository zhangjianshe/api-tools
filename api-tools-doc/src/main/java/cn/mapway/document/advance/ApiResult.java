package cn.mapway.document.advance;

/**
 * ApiResult
 *
 * @author zhangjianshe <zhangjianshe@gmail.com>
 */
public class  ApiResult<T,K> {
    public int code;
    public String message;
    public T data;
    public K arrs;
}
