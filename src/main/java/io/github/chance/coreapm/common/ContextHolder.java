package io.github.chance.coreapm.common;

public class ContextHolder {
    private static final ThreadLocal<RequestContext> holder = new ThreadLocal<>();

    public static void set(RequestContext context) {
        holder.set(context);
    }

    public static RequestContext get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove(); // ⭐ 중요
    }
}