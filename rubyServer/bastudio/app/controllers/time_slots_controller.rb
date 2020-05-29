class TimeSlotsController < ApplicationController
  skip_before_action :verify_authenticity_token

  def new
    @experts = Expert.all
    @expertsProcedures = ExpProcedure.all
  end

  def index
    @experts = Expert.all
    @timeSlots = TimeSlot.all
    @procedures = Procedure.all
    unless params[:date].nil?
      @date = params[:date].to_date;
    else
      @date = Date.today()
    end
      extractDateName

    respond_to  do |format|
      format.html {render 'index'}

        format.json { render :json =>
          unless params[:expert_id].nil?
            Expert.where(:id => params[:expert_id])
            .first
            .time_slots
            .select { |timeSlot| timeSlot.date == Date.today }
            .uniq
            .to_json(:only => [:startTime, :endTime])
          end
        }
    end
  end

  def create
    hours = params[:detail_form]['start_time(4i)']
    minutes = params[:detail_form]['start_time(5i)']
    startTime = (hours.to_s + ':' + minutes.to_s).to_time
    @timeSlot = TimeSlot.new
    @timeSlot[:startTime] = startTime
    @timeSlot[:endTime] = startTime
    @timeSlot[:date] = params[:date]
    @timeSlot[:client_first_name] = params[:client_first_name]
    @timeSlot[:client_phone_number] = params[:client_phone_number]
    @totalPrice = 0
    unless params[:procedures].empty?
      params[:procedures].each {
        |procedureId|
        @procedure = Procedure.where(:id => procedureId).first
        @expProcedure = ExpProcedure.where(:expert_id => params[:expert_id], :procedure_id => procedureId).first
        @totalPrice += @expProcedure[:price].to_i
        @timeSlot.exp_procedures << @expProcedure
        @timeSlot.endTime += @expProcedure[:time_consume].minutes
      }
      @timeSlot[:price] = @totalPrice
    end
    @timeSlot.save
    redirect_to time_slots_path
  end

  def by_date
    render plain: params[:date]
  end

  def get_by_client_phone
    respond_to  do |format|
        format.json { render :json =>
          TimeSlot.where('client_phone_number like ?', params[:phone_number])
          .all
          .filter { |timeSlot| timeSlot.date > Time.now}
          .sort { |a,b| a.date <=> b.date }
          .to_json
        }
    end
  end

  private
  def extractDateName
    case @date.wday
    when 0
      @dayName = 'Воскресенье'
    when 1
      @dayName = 'Понедельник'
    when 2
      @dayName = 'Вторник'
    when 3
      @dayName = 'Среда'
    when 4
      @dayName = 'Четверг'
    when 5
      @dayName = 'Пятница'
    when 6
      @dayName = 'Суббота'
    end
  end

end
