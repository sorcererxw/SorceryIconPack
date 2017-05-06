package com.sorcererxw.sorcery.icons.packtools.leancloud;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/1
 */

public interface LeancloudSearvice {
    @GET("1.1/scan/classes/IconTable")
    Observable<IconTableItem> scanIconTable(@Query("limit") int limit,
                                            @Query("cursor") String cursor);

    @GET("1.1/scan/classes/IconTable")
    Observable<IconTableItem> scanIconTable(@Query("cursor") String cursor);

    @GET("1.1/scan/classes/IconTable")
    Observable<IconTableItem> scanIconTable();
}
