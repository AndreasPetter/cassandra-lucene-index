Performance tips
****************

Lucene index plugin performance varies depending upon several factors
depending on the use case and you should probably do some tuning work.
However, there is some general advice.

Disable virtual nodes
=====================

Although virtual nodes are fully supported, we recommend turning them off.
In the same way as virtual nodes use to be problematic with analytical tools as Spark, Hadoop and Solr,
Lucene indexes performance goes down because each node query is split into several data range sub-queries.

Use a separate disk
===================

You will get better performance using a separate disk for the Lucene index files.
You can set the place where the index will be stored using the `directory_path` option:

.. code-block:: sql

    CREATE CUSTOM INDEX tweets_index ON tweets (lucene)
    USING 'com.stratio.cassandra.lucene.Index'
    WITH OPTIONS = {
        'directory_path' : '<lucene_disk>',
        ...
    };


Index only what you need
========================

The more fields you index, the more resources will be consumed.
So you should carefully study which kind of queries are you going to use before creating the schema.
You should also be careful choosing the ``indexed`` and ``sorted`` options of the mappers,
because each of them creates at least on field per Cassandra column, doing your index larger and slower.

Use a low refresh rate
======================

You can choose any index refresh rate you need,
and you can expect a good behaviour even with a refresh rate of just one second.
The default refresh rate is 60 seconds, which is a pretty conservative value.
However, high refresh rates imply a higher general resources consumption.
So you should use a refresh rate as low as your use case allows.
You can set the refresh rate using the `refresh` option:

.. code-block:: sql

    CREATE CUSTOM INDEX tweets_index ON tweets (lucene)
    USING 'com.stratio.cassandra.lucene.Index'
    WITH OPTIONS = {
        'refresh' : '<refresh_rate>',
        ...
    };

Prefer filters over queries
===========================

Query searches involve relevance so they should be sent to all nodes in the
cluster in order to find the globally best results.
However, filters have a chance to find the results in a subset of the nodes.
So if you are not interested in relevance sorting then you should prefer filters over queries.

Use a large page size
=====================

Cassandra native paging is fully supported even for top-k queries,
and we do not discourage its use in any way.
However getting n rows in a page is always faster than retrieving the same n rows in two or more pages.
For that reason, if you are interested in retrieving the best 200 rows matching a search,
then you should ideally use a page size of 200.
In the other hand, if you want to retrieve thousands or millions of rows,
then you should use a high page size, maybe 1000 rows per page.
Page size can be set in cqlsh in a per-session basis using the command `PAGING``
and in Java driver its set in a per-query basis using the attribute `pageSize``.


