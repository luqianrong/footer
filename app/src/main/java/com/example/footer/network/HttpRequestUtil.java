package com.example.footer.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.inter.ResponseStringDataListener;
import com.android.volley.toolbox.StringRequest;
import com.example.footer.application.ApplicationController;
import com.example.footer.utils.LogUtil;

import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * volley
 *
 * @author
 */
public class HttpRequestUtil {

    private static final String NETWORK_ERROR_CODE = "VOLLEY_ERROR_CODE";


    /**
     * POST
     * @param url
     * @param params
     * @param responseStringDataListener
     * @param taskId
     */
    public static void HttpRequestByPost(
            String url, final Map<String, String> params,
            final ResponseStringDataListener responseStringDataListener, int taskId) {
        if (url == null || params == null || responseStringDataListener == null) {
            LogUtil.e("HttpRequestByPost------>>>received null param");
        }
        postMethodRequest(url, params, responseStringDataListener, taskId);

    }


    /**
     * GET
     *
     * @param url
     * @param responseStringDataListener
     * @param taskId
     */
    public static void HttpRequestByGet(
            String url, final ResponseStringDataListener responseStringDataListener, int taskId) {
        if (url == null || responseStringDataListener == null) {
            LogUtil.e("HttpRequestByGet------>>>received null param");
        }
        getMethodRequest(url,responseStringDataListener,taskId);

    }

    /**
     * get method
     *
     * @param url
     * @return
     */
    private static void getMethodRequest(String url, ResponseStringDataListener responseStringDataListener, int taskId) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(Request request, String response) {
                if (null != request && null != request.getNetResponseDataListener()) {
                    ResponseStringDataListener responseDataListener =
                            (ResponseStringDataListener) request.getNetResponseDataListener();
                    try {
                        responseDataListener.onDataDelivered(request.getTaskId(), response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(Request request, VolleyError error) {
                if (null != request && null != request.getNetResponseDataListener()) {
                    ResponseStringDataListener responseDataListener = (ResponseStringDataListener) request.getNetResponseDataListener();
                    responseDataListener.onErrorHappened(request.getTaskId(), NETWORK_ERROR_CODE, error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        addToQueue(responseStringDataListener, taskId, stringRequest);
    }

    /**
     * post method
     * @param url
     * @param params
     * @param responseStringDataListener
     * @param taskId
     */
    private static void postMethodRequest(final String url, final Map<String, String> params, ResponseStringDataListener responseStringDataListener, int taskId) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(Request request, String response) {
                ResponseStringDataListener responseDataListener = (ResponseStringDataListener) request.getNetResponseDataListener();
                if (null != request && null != request.getNetResponseDataListener()) {
                    try {
                        responseDataListener.onDataDelivered(request.getTaskId(), response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(Request request, VolleyError error) {
                if (null != request && null != request.getNetResponseDataListener()) {
                    ResponseStringDataListener responseDataListener = (ResponseStringDataListener) request.getNetResponseDataListener();
                    responseDataListener.onErrorHappened(request.getTaskId(), NETWORK_ERROR_CODE, error.toString());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        addToQueue(responseStringDataListener, taskId, stringRequest);
    }

    /**
     * add queue
     *
     * @param responseStringDataListener
     * @param taskId
     * @param stringRequest
     */
    private static void addToQueue(ResponseStringDataListener responseStringDataListener, int taskId, StringRequest stringRequest) {
        stringRequest.setTaskId(taskId);
        stringRequest.setNetResponseDataListener(responseStringDataListener);
        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }


    private static Map<String, String> getStringStringMap(Map<String, String> params) {
        Map<String, String> contents = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key == null || value == null) {
                throw new NullPointerException("key == null");
            }
            contents.put(key, value);
        }
        return contents;
    }


}
