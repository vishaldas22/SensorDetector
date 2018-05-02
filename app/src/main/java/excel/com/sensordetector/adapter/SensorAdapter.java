package excel.com.sensordetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import excel.com.sensordetector.R;
import excel.com.sensordetector.model.SensorModel;
import excel.com.sensordetector.util.CommonUtils;

public class SensorAdapter extends BaseAdapter {
    private List<SensorModel> sensorModelList;
    private Context context;

    public SensorAdapter(Context context, List<SensorModel> sensorModel) {
        this.sensorModelList = sensorModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sensorModelList.size();
    }

    @Override
    public Object getItem(int i) {

        return i;
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;

        //holder working on background thread
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.new_list, null);
            holder = new ViewHolder();

            //holder.date = convertView.findViewById(R.id.tv_date);
            //holder.time = convertView.findViewById(R.id.tv_time);
            holder.distance = convertView.findViewById(R.id.tv_distance);
            holder.motion = convertView.findViewById(R.id.tv_motion);
            holder.temperature = convertView.findViewById(R.id.tv_temperature);
            //holder.humidity = convertView.findViewById(R.id.tv_humidity);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        //holder.date.setText(CommonUtils.getDateFromMillSec(sensorModelList.get(position).getDate()));
        //holder.time.setText(CommonUtils.getTimeFromSeconds(sensorModelList.get(position).getTime()));
        //holder.humidity.setText(sensorModelList.get(position).getHumidity()+"");
        holder.distance.setText(sensorModelList.get(position).getDistance());
        holder.temperature.setText(sensorModelList.get(position).getTemperature());
        holder.motion.setText(sensorModelList.get(position).getMotion());

        return convertView;
    }

    public List<SensorModel> getList() {
        return sensorModelList;
    }

    private class ViewHolder {
        TextView date, time, temperature, humidity, distance, motion;

    }
}